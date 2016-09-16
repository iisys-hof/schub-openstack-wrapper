package de.hofuniversity.iisys.schub.openstack.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.model.EntityState;
import de.hofuniversity.iisys.schub.openstack.model.Tenant;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.monitoring.TenantMonitor;
import de.hofuniversity.iisys.schub.openstack.processing.ServiceHandler;
import de.hofuniversity.iisys.schub.openstack.processing.TenantProcessor;
import de.hofuniversity.iisys.schub.openstack.util.ConfigLoader;
import de.hofuniversity.iisys.schub.openstack.util.ConfigStorage;

public class WrapperService
{
    private static WrapperService fInstance;

    private final ConfigLoader fConfigLoader;
    
    private final WrapperConfig fWrapperConfig;
    private final ClusterConfig fClusterConfig;
    
    private final ConfigStorage fConfigStorage;
    
    private final TenantProcessor fTenantProc;
    private final ServiceHandler fServiceHandler;
    
    private final TenantMonitor fMonitor;
    
    private final Map<String, Tenant> fTenants;
    private final Map<String, VirtualMachine> fVMs;
    
    public static WrapperService getInstance()
    {
        if(fInstance == null)
        {
            fInstance = new WrapperService();
        }
        
        return fInstance;
    }
    
    
    public WrapperService()
    {
        fTenants = new HashMap<String, Tenant>();
        fVMs = new HashMap<String, VirtualMachine>();
        
        fConfigLoader = new ConfigLoader();
        fWrapperConfig = fConfigLoader.getWrapperConfig();
        fClusterConfig = fConfigLoader.getClusterConfig();
        
        fConfigStorage = new ConfigStorage(fWrapperConfig);
        
        for(TenantConfig tc : fConfigLoader.getTenantConfigs())
        {
            Tenant tenant = new Tenant(tc);
            fTenants.put(tc.getfTenantId(), tenant);
            
            for(VMConfig vmc : tc.getfVirtualMachines())
            {
                VirtualMachine vm = new VirtualMachine(vmc, tc.getfTenantId());
                fVMs.put(vmc.getfId(), vm);
            }
        }
        
        fTenantProc = new TenantProcessor(this);
        fServiceHandler = new ServiceHandler(this);
        
        fMonitor = new TenantMonitor(this);
    }
    
    public ConfigLoader getConfigLoader()
    {
        return fConfigLoader;
    }
    
    public ClusterConfig getClusterConfig()
    {
        return fClusterConfig;
    }
    
    public WrapperConfig getWrapperConfig()
    {
        return fWrapperConfig;
    }
    
    public Map<String, Tenant> getTenants()
    {
        return fTenants;
    }
    
    public Map<String, VirtualMachine> getVMs()
    {
        return fVMs;
    }
    
    public void newTenant(Tenant tenant) throws Exception
    {
        String id = tenant.getConfig().getfTenantId();
        String name = tenant.getConfig().getfTenantName();
        String conName = tenant.getConfig().getfTenantConsoleName();
        
        // check if tenant with that name already exists
        for(Entry<String, Tenant> tenE : fTenants.entrySet())
        {
            TenantConfig tenEconf = tenE.getValue().getConfig();
            
            if(id.equalsIgnoreCase(tenEconf.getfTenantId())
                || name.equalsIgnoreCase(tenEconf.getfTenantName())
                || conName.equalsIgnoreCase(tenEconf.getfTenantConsoleName()))
            {
                throw new Exception("tenant naming conflict");
            }
        }

        tenant.setState(EntityState.NEW);
        fTenants.put(id, tenant);
        
        // initialize Environment for tenant
        fTenantProc.initTenant(tenant);
        
        // register virtual machines
        for(Entry<String,VirtualMachine> vmE : tenant.getVMs().entrySet())
        {
            fVMs.put(vmE.getKey(), vmE.getValue());
        }
        
        // store definition in file system
        fConfigStorage.storeTenant(tenant.getConfig());
    }
    
    public void newVM(VirtualMachine vm) throws Exception
    {
        vm.setState(EntityState.NEW);
        String id = vm.getConfig().getfId();
        
        //add to tenant
        Tenant tenant = fTenants.get(vm.getTenantId());
        if(tenant == null)
        {
            throw new Exception("Tenant with ID " + vm.getTenantId() + " not found");
        }
        tenant.getVMs().put(id, vm);
        
        //add configuration to tenant configuration
        tenant.getConfig().getfVirtualMachines().add(vm.getConfig());
        
        //add to VM map
        fVMs.put(id, vm);
        
        // TODO: start virtual machine?
        fTenantProc.newVM(vm);
        
        // store updated tenant definition in file system
        fConfigStorage.storeTenant(tenant.getConfig());
    }
    
    public void updateTenant(TenantConfig config) throws Exception
    {
        //TODO: determine differences to existing configuration and adjust accordingly
        //TODO: can this include VMs?
        
        // TODO: replace configuration locally
        Tenant tenant = fTenants.get(config.getfTenantId());
        tenant.setConfig(config);
        
        // store updated tenant definition in file system
        fConfigStorage.storeTenant(config);
    }
    
    public void updateVM(VMConfig config) throws Exception
    {
        VirtualMachine vm = fVMs.get(config.getfId());
        Tenant tenant = fTenants.get(vm.getTenantId());
        
        // TODO: actually update configuration and scale or re-create
        vm.setConfig(config);
        
        // store updated tenant definition in file system
        fConfigStorage.storeTenant(tenant.getConfig());
    }
    
    public void deleteTenant(String tenantId) throws Exception
    {
        Tenant tenant = fTenants.get(tenantId);
        if(tenant == null)
        {
            throw new Exception("Tenant with ID " + tenantId + " not found");
        }
        
        TenantConfig tc = tenant.getConfig();
        
        // tear down enviroment?
        fTenantProc.deleteTenant(tenant);

        // TODO: stop all VMs
        // TODO: remove all VMs
        for(VMConfig vmc : tc.getfVirtualMachines())
        {
            String vmId = vmc.getfId();
            
            fVMs.remove(vmId);
        }
        
        // remove tenant
        fTenants.remove(tenantId);

        // delete file from disk
        fConfigStorage.removeTenant(tc);
    }
    
    public void deleteVM(String vmId) throws Exception
    {
        VirtualMachine vm = fVMs.get(vmId);
        if(vm == null)
        {
            throw new Exception("Virtual Machine with ID " + vmId + " not found");
        }
        
        // TODO: stop and delete virtual machine?
        fTenantProc.deleteVM(vm);
        
        // TODO: remove from tenant
        Tenant tenant = fTenants.get(vm.getTenantId());
        tenant.getVMs().remove(vmId);
        
        // remove from tenant configuration
        tenant.getConfig().getfVirtualMachines().remove(vm.getConfig());
        
        // remove
        fVMs.remove(vmId);
        
        // update tenant configuration on disk
        fConfigStorage.storeTenant(tenant.getConfig());
    }
    
    public void createService(String service, String tenantId,
        String vmId, Map<String, String> parameters) throws Exception
    {
        TenantConfig tc = fTenants.get(tenantId).getConfig();
        
        VirtualMachine vm = fVMs.get(vmId);
        
        if(vm == null)
        {
            throw new Exception("VM with ID '" + vmId + "' not found");
        }
        
        VMConfig vmc = vm.getConfig();
        
        fServiceHandler.createService(service, tc, vmc, parameters);
        
        // update tenant configuration on disk
        fConfigStorage.storeTenant(tc);
    }
    
    public void deleteService(String service, String tenantId) throws Exception
    {
        TenantConfig tc = fTenants.get(tenantId).getConfig();
        
        // TODO: find the VM running this service
        VMConfig vmc = null;
        
        for(VMConfig config : tc.getfVirtualMachines())
        {
            if(config.getfServices().contains(service))
            {
                vmc = config;
                break;
            }
        }
        
        fServiceHandler.deleteService(service, tc, vmc);
        
        // update tenant configuration on disk
        fConfigStorage.storeTenant(tc);
    }
    
    public Map<String, String> getServiceStates(String tenantId) throws Exception
    {
        Tenant tenant = fTenants.get(tenantId);
        
        if(tenant == null)
        {
            throw new Exception("tenant with ID '" + tenantId + "' not found");
        }
        
        TenantConfig tc = tenant.getConfig();
        
        return fServiceHandler.getServiceStatus(tc);
    }
    
    public void shutdown()
    {
        fMonitor.stop();
        
        // TODO: write current tenant configurations to disk?
    }
}
