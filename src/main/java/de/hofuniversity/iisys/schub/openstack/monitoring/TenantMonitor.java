package de.hofuniversity.iisys.schub.openstack.monitoring;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.model.EntityState;
import de.hofuniversity.iisys.schub.openstack.model.Tenant;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.util.ProcessUtility;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class TenantMonitor implements Runnable
{
    private final Object fTrigger;
    
    private final Map<String, Tenant> fTenants;
    private final Map<String, VirtualMachine> fVMs;
    
    private final File fTmpDirectory;
    
    private final String fSshKeyFile;
    private final String fSshUser;
    
    private final Logger fLogger;
    
    private final long fInitWait;
    private final long fVmPollInt;
    
    private boolean fActive;
    
    public TenantMonitor(WrapperService wrapperSvc)
    {
        fActive = true;
        
        fTrigger = new Object();
        
        fTenants = wrapperSvc.getTenants();
        fVMs = wrapperSvc.getVMs();
        
        WrapperConfig wrapConf = wrapperSvc.getWrapperConfig();
        fTmpDirectory = new File(wrapConf.getfTmpDirectory());
        
        fSshKeyFile = wrapConf.getfSshKey();
        fSshUser = wrapConf.getfSshUser();
        
        fInitWait = wrapConf.getfMonitoringInitWait();
        fVmPollInt = wrapConf.getfMonitoringVmPollInterval();
        
        fLogger = Logger.getLogger(this.getClass().getName());
        
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(fInitWait);
        }
        catch (InterruptedException e)
        {
            fLogger.log(Level.WARNING, "tenant monitor exception", e);
        }
        
        while(fActive)
        {
            try
            {
                // check tenant general status
                // TODO: how to do properly?
                for(Tenant tenant : fTenants.values())
                {
                    checkTenantStatus(tenant);
                }
                
                // check status of virtual machines
                for(VirtualMachine vm : fVMs.values())
                {
                    checkVMStatus(vm);
                }
                
                synchronized(fTrigger)
                {
                    fTrigger.wait(fVmPollInt);
                }
            }
            catch(Exception e)
            {
                fLogger.log(Level.WARNING, "tenant monitor exception", e);
            }
        }
    }
 
    public void stop()
    {
        fActive = false;
        
        synchronized(fTrigger)
        {
            fTrigger.notify();
        }
    }
    
    private void checkTenantStatus(Tenant tenant)
    {
        EntityState state = tenant.getState();
        
        if(state == EntityState.UNKNOWN)
        {
            // TODO: check for LDAP entries?
            
            // TODO: check SQL databases?
            
            // TODO: check for certificates?
            
            // TODO: check for generated service configurations
            int configEntries = tenant.getConfig().getfServiceConfigs().size();
            if(configEntries >= ServiceConstants.SERVICES.length)
            {
                tenant.setState(EntityState.RUNNING);
            }
        }
    }
    
    private void checkVMStatus(VirtualMachine vm)
    {
        EntityState state = vm.getState();
        
        if(state == EntityState.UNKNOWN
            || state == EntityState.OFFLINE
            || state == EntityState.RUNNING)
        {
            // TODO: check if stack exists?
            
            // try executing echo, see if VM is online
            List<String> ips = vm.getConfig().getfIpAddresses();
            String response = "";
            
            try
            {
                String ip = ips.get(0);
                String command = "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i "
                    + fSshKeyFile + " " + fSshUser + "@" + ip + " " + "echo success";
                
                response = ProcessUtility.getOuput(command, fTmpDirectory);
            }
            catch(Exception e)
            {
                fLogger.log(Level.WARNING, "could not ping VM " + vm.getConfig().getfId(), e);
            }
                
            if(!response.isEmpty())
            {
                vm.setState(EntityState.RUNNING);
            }
            else
            {
                vm.setState(EntityState.OFFLINE);
            }
        }
    }
}
