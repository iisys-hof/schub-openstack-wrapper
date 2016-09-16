package de.hofuniversity.iisys.schub.openstack.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;
import de.hofuniversity.iisys.schub.openstack.config.gen.ConfigGenerator;
import de.hofuniversity.iisys.schub.openstack.model.Tenant;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;
import de.hofuniversity.iisys.schub.openstack.util.TenantConfigConverter;
import de.hofuniversity.iisys.schub.openstack.util.ValueValidator;

/**
 * API servlet supplying tenant information and triggering creation,
 * deletion or changing of tenants.
 */
public class RestService extends HttpServlet 
{
    public static final String VM_ID_PARAM = "vmId";
    
    private static final String STATE_FIELD = "state";
    
    private static final String TENANTS_PATH = "/tenants";
    private static final String VMS_PATH = "/machines";
    private static final String SERVICES_PATH = "/services";

    private final WrapperService fService;
    private final ConfigGenerator fConfigGenerator;
    private final ValueValidator fValidator;
    
    //TODO: require authentication from user
    
    public RestService()
    {
        fService = WrapperService.getInstance();
        fConfigGenerator = new ConfigGenerator();
        fValidator = new ValueValidator();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String path = request.getPathInfo();
        
        if(path == null)
        {
            path = "/";
        }
        
        if(path.startsWith(TENANTS_PATH))
        {
            getTenants(request, response);
        }
        else if(path.startsWith(VMS_PATH))
        {
            getVms(request, response);
        }
        else if(path.startsWith(SERVICES_PATH))
        {
            getServices(request, response);
        }
        else if("/".equals(path))
        {
            PrintWriter out = response.getWriter();

            out.println("SChub Openstack Wrapper");
            out.println("available endpoints:");
            out.println("\t" + TENANTS_PATH + "/");
            out.println("\t" + VMS_PATH + "/");
            out.println("\t" + SERVICES_PATH + "/");

            out.close();
        }
        else
        {
            PrintWriter out = response.getWriter();
            
            response.setStatus(404);
            out.println("Path " + path + " not bound");
            
            out.close();
        }
    }
    
    private String getTextInput(HttpServletRequest request) throws IOException
    {
        String text = "";
        String line = null;
        
        BufferedReader br = new BufferedReader(
            new InputStreamReader(request.getInputStream()));
        
        line = br.readLine();
        while(line != null)
        {
            text += line;
            line = br.readLine();
        }
        
        br.close();
        
        return text;
    }
    
    private String getSecondElem(HttpServletRequest request, String firstElem)
    {
        // get the second element in the path
        String secondElem = null;
        
        String pathInf = request.getPathInfo();
        if(pathInf.contains(firstElem)
            && pathInf.length() > (firstElem).length())
        {
            secondElem = pathInf.substring((firstElem).length());
            
            if(secondElem.contains("/"))
            {
                secondElem = secondElem.substring(0, secondElem.indexOf('/'));
            }
        }
        
        return secondElem;
    }
    
    private void getTenants(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        
        //determine if a specific ID is requested
        String idParam = getSecondElem(request, TENANTS_PATH + "/");
        
        try
        {
            if(idParam != null)
            {
                // get the details of a specific tenant
                getTenantDetails(idParam, out);
            }
            else
            {
                //return list of all tenants
                getAllTenants(out);
            }
            
            response.setContentType("application/json");
        }
        catch (Exception e)
        {
            response.setStatus(500);
            response.resetBuffer();
            e.printStackTrace();
            out.println(e.toString());
        }
        out.close();
    }
    
    private void getTenantDetails(String idParam, PrintWriter out) throws Exception
    {
        Tenant tenant = fService.getTenants().get(idParam);
        if(tenant != null)
        {
            TenantConfig tc = tenant.getConfig();
            
            JSONObject entry = new JSONObject();
            entry.put(TenantConfig.ID_PROP, tc.getfTenantId());
            entry.put(TenantConfig.NAME_PROP, tc.getfTenantName());
            entry.put(TenantConfig.CON_NAME_PROP, tc.getfTenantConsoleName());
            entry.put(TenantConfig.DESC_PROP, tc.getfTenantDescription());
            entry.put(TenantConfig.AVAIL_ZONE_PROP, tc.getfAvailZone());
            entry.put(STATE_FIELD, tenant.getState().toString());
            
            JSONArray vms = new JSONArray();
            for(VMConfig vmConfig : tc.getfVirtualMachines())
            {
                vms.put(vmConfig.getfId());
            }
            entry.put(TenantConfig.VMS_PROP, vms);
            
            out.println(entry.toString());
        }
        else
        {
            throw new Exception("Tenant with ID " + idParam + " not found");
        }
    }
    
    private void getAllTenants(PrintWriter out) throws Exception
    {
        JSONArray tenantArr = new JSONArray();
        
        for(Entry<String, Tenant> tenE : fService.getTenants().entrySet())
        {
            JSONObject entry = new JSONObject();
            entry.put(TenantConfig.ID_PROP, tenE.getValue().getConfig().getfTenantId());
            entry.put(TenantConfig.NAME_PROP, tenE.getValue().getConfig().getfTenantName());
            entry.put(TenantConfig.CON_NAME_PROP, tenE.getValue().getConfig().getfTenantConsoleName());
            tenantArr.put(entry);
        }
      
        out.println(tenantArr.toString());
    }
    
    private void getVms(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        
        //determine if a specific ID is requested
        String idParam = getSecondElem(request, VMS_PATH + "/");
        
        try
        {
            if(idParam != null)
            {
                //ID parameter as comma separated list?
                String[] idArray = idParam.split(",");
                JSONArray vmArray = new JSONArray();
                
                if(idArray.length > 1)
                {
                    //retrieve an array of VMs
                    for(String id : idArray)
                    {
                        vmArray.put(encodeVM(id));
                    }

                    out.println(vmArray.toString());
                }
                else
                {
                    //get single entry by ID
                    JSONObject entry = encodeVM(idArray[0]);
                    out.println(entry.toString());
                }
            }
            else
            {
                JSONArray vmArr = new JSONArray();
                
                //retrieve a list of all virtual machines
                for(Entry<String, VirtualMachine> vmE : fService.getVMs().entrySet())
                {
                    VMConfig vmc = vmE.getValue().getConfig();
                    
                    JSONObject entry = new JSONObject();
                    entry.put(VMConfig.ID_PROP, vmc.getfId());
                    entry.put(VMConfig.NAME_PROP, vmc.getfName());
                    entry.put(VirtualMachine.TENANT_ID_PROP, vmE.getValue().getTenantId());
                    
                    vmArr.put(entry);
                }
                
                //TODO: sort?
                
                out.println(vmArr.toString());
            }
        
            response.setContentType("application/json");
        }
        catch (Exception e)
        {
          response.setStatus(500);
          response.resetBuffer();
          e.printStackTrace();
          out.println(e.toString());
        }
        
        out.close();
    }
    
    private JSONObject encodeVM(String id) throws Exception
    {
        // get the details of a specific virtual machines
        VirtualMachine vm = fService.getVMs().get(id);
        JSONObject entry = new JSONObject();
        if(vm != null)
        {
            VMConfig vmc = vm.getConfig();
            
            entry.put(VMConfig.ID_PROP, vmc.getfId());
            entry.put(VMConfig.NAME_PROP, vmc.getfName());
            entry.put(VirtualMachine.TENANT_ID_PROP, vm.getTenantId());
            entry.put(VMConfig.FLAVOR_NAME_PROP, vmc.getfFlavorName());
            entry.put(VMConfig.IMAGE_NAME_PROP, vmc.getfImageName());
            entry.put(STATE_FIELD, vm.getState().toString());
            
            //ip addresses
            JSONArray addArr = new JSONArray();
            for(String add : vmc.getfIpAddresses())
            {
                addArr.put(add);
            }
            entry.put(VMConfig.IP_ADDRS_PROP, addArr);
            
            //activated services
            JSONArray svcArr = new JSONArray();
            for(String svc : vmc.getfServices())
            {
                svcArr.put(svc);
            }
            entry.put(VMConfig.SERVICES_PROP, svcArr);
        }
        else
        {
            throw new Exception("VM with ID " + id + " not found");
        }
        
        return entry;
    }
    
    private void getServices(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        PrintWriter out = response.getWriter();
        
        // get specified tenant ID
        String tenantId = getSecondElem(request, SERVICES_PATH + "/");
        
        // TODO: get specified service
        String service = getSecondElem(request, SERVICES_PATH + "/" + tenantId + "/");
        
        try
        {
            if(tenantId == null)
            {
                // return a list of all known services
                JSONArray list = new JSONArray();
                for(String svc : ServiceConstants.SERVICES)
                {
                    list.put(svc);
                }
                out.println(list.toString());
            }
            else if(service == null)
            {
                // return service status
                Map<String, String> status = fService.getServiceStates(tenantId);
                JSONArray array = TenantConfigConverter.toJSON(status);
                
                out.println(array);
            }
            else
            {
                // return service configuration
                Tenant tenant = fService.getTenants().get(tenantId);
                if(tenant != null)
                {
                    Map<String, String> svcConf = tenant.getConfig()
                        .getfServiceConfigs().get(service);
                    
                    if(svcConf != null)
                    {
                        JSONArray conf = TenantConfigConverter.toJSON(svcConf);
                        out.println(conf);
                    }
                    else
                    {
                        throw new Exception("Service with name '" + service + "' not found");
                    }
                }
                else
                {
                    throw new Exception("Tenant with ID '" + tenantId + "' not found");
                }
            }
        
            response.setContentType("application/json");
        }
        catch (Exception e)
        {
          response.setStatus(500);
          response.resetBuffer();
          e.printStackTrace();
          out.println(e.toString());
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //TODO: update tenant
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //TODO: create tenant or virtual machine
        String path = request.getPathInfo();
        
        if(path == null)
        {
            path = "/";
        }
        
        if(path.startsWith(TENANTS_PATH))
        {
            createTenant(request, response);
        }
        else if(path.startsWith(VMS_PATH))
        {
            createVM(request, response);
        }
        else if(path.startsWith(SERVICES_PATH))
        {
            createService(request, response);
        }
        else
        {
            PrintWriter out = response.getWriter();
            
            response.setStatus(404);
            out.println("Path " + path + " not bound for POST operation");
            
            out.close();
        }
    }
    
    private void createTenant(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String json = getTextInput(request);
        
        // initialize from supplied data
        Tenant tenant = null;
        try
        {
            // convert received JSON and validate
            TenantConfig tc = TenantConfigConverter.fromJSON(json);
            fValidator.validateTenantConfig(tc);
            
            // initialize configuration
            tenant = fConfigGenerator.initTenant(tc);
            
            // add to system
            fService.newTenant(tenant);
            
            // TODO: return ID or whole JSON object?
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    private void createVM(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String json = getTextInput(request);
        
        // initialize from supplied data
        try
        {
            // convert and validate VM definition
            JSONObject jsonObj = new JSONObject(json);
            VMConfig vmc = TenantConfigConverter.readVmConfig(jsonObj);
            String tenantId = jsonObj.optString(VirtualMachine.TENANT_ID_PROP);

            // filter faulty empty and null service strings
            List<String> toRemove = new LinkedList<String>();
            for(String service : vmc.getfServices())
            {
                if(service == null || service.isEmpty())
                {
                    toRemove.add(service);
                }
            }
            vmc.getfServices().removeAll(toRemove);
            
            fValidator.validateVMConfig(vmc);
            
            if(fService.getTenants().get(tenantId) == null)
            {
                throw new Exception("Tenant with ID '" + tenantId + "' not found");
            }
            
            // initialize VM configuration
            VirtualMachine vm = fConfigGenerator.initVM(vmc, tenantId);
            
            // add to system
            fService.newVM(vm);
            
            // TODO: return ID or whole JSON object?
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    private void createService(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String json = getTextInput(request);
        
        String tenantId = getSecondElem(request, SERVICES_PATH + "/");
        
        String service = getSecondElem(request, SERVICES_PATH + "/" + tenantId + "/");
        
        // initialize from supplied data
        try
        {
            Map<String, String> params = TenantConfigConverter
                .readServiceConf(new JSONArray(json));
            
            String vmId = params.remove(VM_ID_PARAM);
            fService.createService(service, tenantId, vmId, params);
            
            // TODO: what to return?
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //TODO: delete tenant or virtual machine
        String path = request.getPathInfo();
        
        if(path == null)
        {
            path = "/";
        }
        
        if(path.startsWith(TENANTS_PATH))
        {
            deleteTenant(request, response);
        }
        else if(path.startsWith(VMS_PATH))
        {
            deleteVM(request, response);
        }
        else if(path.startsWith(SERVICES_PATH))
        {
            deleteService(request, response);
        }
        else
        {
            PrintWriter out = response.getWriter();
            
            response.setStatus(404);
            out.println("Path " + path + " not bound for POST operation");
            
            out.close();
        }
    }
    
    private void deleteTenant(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //get specified ID
        String idParam = getSecondElem(request, TENANTS_PATH + "/");
        if(idParam == null)
        {
            PrintWriter out = response.getWriter();
            response.setStatus(400);
            out.println("no ID specified");
            out.close();
        }
        
        try
        {
            //remove from system
            fService.deleteTenant(idParam);
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    private void deleteVM(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //get specified ID
        String idParam = getSecondElem(request, VMS_PATH + "/");
        if(idParam == null)
        {
            PrintWriter out = response.getWriter();
            response.setStatus(400);
            out.println("no ID specified");
            out.close();
        }
        
        try
        {
            //remove from system
            fService.deleteVM(idParam);
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
    
    private void deleteService(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //get specified ID
        
        String tenantId = getSecondElem(request, SERVICES_PATH + "/");
        
        String service = getSecondElem(request, SERVICES_PATH + "/" + tenantId + "/");
        
        if(tenantId == null)
        {
            PrintWriter out = response.getWriter();
            response.setStatus(400);
            out.println("no ID specified");
            out.close();
        }
        
        if(service == null)
        {
            PrintWriter out = response.getWriter();
            response.setStatus(400);
            out.println("no service specified");
            out.close();
        }
        
        try
        {
            //remove from system
            fService.deleteService(service, tenantId);
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }
}
