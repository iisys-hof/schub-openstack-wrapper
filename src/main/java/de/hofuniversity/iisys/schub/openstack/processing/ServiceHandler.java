package de.hofuniversity.iisys.schub.openstack.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.services.CamundaConf;
import de.hofuniversity.iisys.schub.openstack.services.CasConf;
import de.hofuniversity.iisys.schub.openstack.services.ElasticsearchConf;
import de.hofuniversity.iisys.schub.openstack.services.EsRelayConf;
import de.hofuniversity.iisys.schub.openstack.services.IServiceConfigurator;
import de.hofuniversity.iisys.schub.openstack.services.LiferayConf;
import de.hofuniversity.iisys.schub.openstack.services.NginxConf;
import de.hofuniversity.iisys.schub.openstack.services.NuxeoConf;
import de.hofuniversity.iisys.schub.openstack.services.OpenXchangeConf;
import de.hofuniversity.iisys.schub.openstack.services.ShindigConf;
import de.hofuniversity.iisys.schub.openstack.services.WebsocketConf;
import de.hofuniversity.iisys.schub.openstack.util.HttpUtil;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;
import de.hofuniversity.iisys.schub.openstack.util.TemplateProcessor;

/**
 * Handles creation and modification of tenant services on VMs via Marathon
 * over Mesos.
 */
public class ServiceHandler
{
    private static final String MAR_APP_FIELD = "app";
    private static final String MAR_STAGED_FIELD = "tasksStaged";
    private static final String MAR_RUNNING_FIELD = "tasksRunning";
    
    private final WrapperService fWrapperSvc;
    private final WrapperConfig fWrapperConf;
    private final ClusterConfig fClusterConf;
    
    private final String fTemplateDir;
    private final String fTmpDir;
    
    private final String fMarathonUrl;
    
    private final List<IServiceConfigurator> fConfigurators;
    
    private final Logger fLogger;
    
    public ServiceHandler(WrapperService wrapperSvc)
    {
        fWrapperSvc = wrapperSvc;
        fWrapperConf = fWrapperSvc.getWrapperConfig();
        fClusterConf = fWrapperSvc.getClusterConfig();
        
        fTemplateDir = fWrapperConf.getfMarTempDirectory();
        fTmpDir = fWrapperConf.getfTmpDirectory();
        
        fMarathonUrl = fClusterConf.getfMarathonUrl();
        
        fConfigurators = new ArrayList<IServiceConfigurator>();
        
        // register all known service configurators
        fConfigurators.add(new CamundaConf());
        fConfigurators.add(new CasConf());
        fConfigurators.add(new ElasticsearchConf());
        fConfigurators.add(new LiferayConf());
        fConfigurators.add(new NuxeoConf());
        fConfigurators.add(new OpenXchangeConf());
        fConfigurators.add(new ShindigConf());
        fConfigurators.add(new WebsocketConf());
        fConfigurators.add(new NginxConf());
        fConfigurators.add(new EsRelayConf());
        
        fLogger = Logger.getLogger(this.getClass().getName());
    }
    
    public Map<String, String> getServiceStatus(TenantConfig tc) throws Exception
    {
        Map<String, String> serviceStatus = new HashMap<String, String>();
        Map<String, Map<String, String>> svcConfs = tc.getfServiceConfigs();
        
        for(String service : ServiceConstants.SERVICES)
        {
            Map<String, String> svcConf = svcConfs.get(service);
            
            if(svcConf != null)
            {
                // get status by marathon service name
                String marName = svcConf.get(ServiceConstants.SERVICE_ID_PROP);
                String status = getServiceStatus(marName);
                serviceStatus.put(service, status);
            }
            else
            {
                serviceStatus.put(service, "missing configuration");
            }
        }
        
        return serviceStatus;
    }
    
    private String getServiceStatus(String marName)
    {
        String status = ServiceConstants.STATUS_ERROR;
        String url = fMarathonUrl + "v2/apps/" + marName;
        
        try
        {
            JSONObject response = new JSONObject(HttpUtil.get(url));
            
            // does not exist
            if(!response.has(MAR_APP_FIELD))
            {
                status = ServiceConstants.STATUS_STOPPED;
            }
            else
            {
                int staged = response.getJSONObject(MAR_APP_FIELD)
                    .getInt(MAR_STAGED_FIELD);
                
                // staged for deployment
                if(staged > 0)
                {
                    status = ServiceConstants.STATUS_STAGED;
                }
                
                // deployed and running
                int running = response.getJSONObject(MAR_APP_FIELD)
                    .getInt(MAR_RUNNING_FIELD);
                if(running > 0)
                {
                    status = ServiceConstants.STATUS_RUNNING;
                }
                
                // waiting to be deployed
                if(running == 0 && staged == 0)
                {
                    status = ServiceConstants.STATUS_WAITING;
                }
            }
        }
        catch(FileNotFoundException fnfe)
        {
            status = ServiceConstants.STATUS_STOPPED;
        }
        catch(Exception e)
        {
            fLogger.log(Level.WARNING,
                "failed to get marathon status for " + marName, e);
        }
        
        return status;
    }
    
    public void initTenant(TenantConfig config) throws Exception
    {
        updateVMConfigs(config);
        
        // trigger individual service autoconfiguration routines
        for(IServiceConfigurator conf : fConfigurators)
        {
            conf.configure(config, fWrapperConf, fClusterConf);
        }

        Map<String, Map<String, String>> svcConfigs
            = config.getfServiceConfigs();
        
        // create services via mesos/marathon
        for(Entry<String, Map<String, String>> svcE : svcConfigs.entrySet())
        {
            // filter non-marathon services and services without VMs
            if(svcE.getValue().containsKey(
                ServiceConstants.SERVICE_TENANT_VM_PROP))
            {
                try
                {
                    spawn(svcE.getKey(), svcE.getValue());
                }
                catch(Exception e)
                {
                    fLogger.log(Level.SEVERE,
                        "failed to spawn service " + svcE.getKey(), e);
                }
            }
        }
    }
    
    private void updateVMConfigs(TenantConfig config) throws Exception
    {
        Map<String, Map<String, String>> svcConfigs
            = config.getfServiceConfigs();
    
        String consoleName = config.getfTenantConsoleName();

        List<VMConfig> vms = config.getfVirtualMachines();
        for(VMConfig vm : vms)
        {
            // configure services to run on the right machine
            String vmName = vm.getfName();
            
            for(String service : vm.getfServices())
            {
                Map<String, String> svcConf = svcConfigs.get(service);
                
                if(svcConf != null)
                {
                    svcConf.put(ServiceConstants.SERVICE_TENANT_PROP, consoleName);
                    svcConf.put(ServiceConstants.SERVICE_TENANT_VM_PROP, vmName);
                }
                else if(service != null && !service.isEmpty())
                {
                    fLogger.log(Level.SEVERE, "Service '" + service + "' not found");
                }
            }
        }
        
        // trigger individual service configuration routines
        for(IServiceConfigurator conf : fConfigurators)
        {
            conf.configure(config, fWrapperConf, fClusterConf);
        }
    }
    
    private void spawn(String name, Map<String, String> config) throws Exception
    {
        String template = fTemplateDir + name + ".json";
        String tmp = fTmpDir + name + ".json";
        
        // fill template
        TemplateProcessor tmpProc = new TemplateProcessor(template);
        tmpProc.setValues(config);
        tmpProc.writeTo(tmp);
        
        // spawn using marathon
        String url = fMarathonUrl + "v2/apps";
        
        String response = HttpUtil.sendJsonFile(tmp, url);
        
        fLogger.log(Level.INFO, "created service " + name + ", result:\n" + response);
        
        // clean up
        new File(tmp).delete();
    }
    
    public void newVM(VirtualMachine vm) throws Exception
    {
        VMConfig vmc = vm.getConfig();
        TenantConfig tc = fWrapperSvc.getTenants().get(vm.getTenantId()).getConfig();
        
        // refresh configurations for new VM
        // TODO: avoid collisions with services on other VMs
        updateVMConfigs(tc);

        // start services for new VM
        Map<String, Map<String, String>> svcConfigs
            = tc.getfServiceConfigs();
        
        List<String> vmSvcs = vmc.getfServices();
        
        // spawn services
        for(String service : vmSvcs)
        {
            Map<String, String> config = svcConfigs.get(service);
            try
            {
                spawn(service, config);
            }
            catch(Exception e)
            {
                fLogger.log(Level.SEVERE, "failed to spawn service " + service, e);
            }
        }
    }
    
    public void deleteTenant(TenantConfig config) throws Exception
    {
        // TODO: delete all possible services
        Set<String> services = config.getfServiceConfigs().keySet();
        for(String svc : services)
        {
            // TODO: send deletion request to Marathon
            deleteService(svc, config);
        }
    }
    
    public void deleteVM(VirtualMachine vm) throws Exception
    {
        TenantConfig tc = fWrapperSvc.getTenants().get(vm.getTenantId()).getConfig();
        
        // TODO: delete all services for this VM
        for(String svc : vm.getConfig().getfServices())
        {
            deleteService(svc, tc);
        }
    }
    
    private void deleteService(String name, TenantConfig tc)
    {
        Map<String, String> svcConf = tc.getfServiceConfigs().get(name);
        
        try
        {
            String marName = svcConf.get(ServiceConstants.SERVICE_ID_PROP);
            String url = fMarathonUrl + "v2/apps/" + marName;
            HttpUtil.sendDelete(url);
        }
        catch(Exception e)
        {
            fLogger.log(Level.SEVERE, "failed to delete service " + name, e);
        }
    }
    
    public void createService(String name, TenantConfig tc, VMConfig vmc,
        Map<String, String> parameters) throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs().get(name);
        
        // TODO: check and set supplied parameters
        for(Entry<String, String> paramE : parameters.entrySet())
        {
            svcConf.put(paramE.getKey(), paramE.getValue());
        }
        
        // TODO: configure to run on correct VM
        svcConf.put(ServiceConstants.SERVICE_TENANT_VM_PROP, vmc.getfName());
        
        // add to VM's service list
        // block services from being started twice
        if(!vmc.getfServices().contains(name))
        {
            vmc.getfServices().add(name);
            
            // actually start service
            try
            {
                spawn(name, svcConf);
            }
            catch(Exception e)
            {
                // remove name again so a previous entry for an unstarted
                // service won't block a new try
                vmc.getfServices().remove(name);
                throw e;
            }
        }
    }
    
    public void deleteService(String name, TenantConfig tc, VMConfig vmc)
    {
        // TODO: remove from VM and remove host from configuration
        if(vmc != null)
        {
            vmc.getfServices().remove(name);
        }
        
        Map<String, String> svcConf = tc.getfServiceConfigs().get(name);
        svcConf.remove(ServiceConstants.SERVICE_TENANT_VM_PROP);
        
        // delete from mesos
        deleteService(name, tc);
    }
}
