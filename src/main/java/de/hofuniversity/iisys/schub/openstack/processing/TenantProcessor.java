package de.hofuniversity.iisys.schub.openstack.processing;

import java.io.File;
import java.util.logging.Logger;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.model.EntityState;
import de.hofuniversity.iisys.schub.openstack.model.Tenant;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.util.ProcessUtility;

/**
 * Handles creation, expansion, reduction and deletion of tenants, their
 * virtual machines and associated cluster resources.
 */
public class TenantProcessor
{
    private final VMHandler fVmHandler;
    
    private final LdapHandler fLdapHandler;
    
    private final SqlHandler fSqlHandler;
    
    private final MailHandler fMailHandler;
    
    private final ServiceHandler fServiceHandler;
    
    private final File fScriptsDir;
    private final String fGenCertsScript;
    
    private final Logger fLogger;
    
    public TenantProcessor(WrapperService wrapperSvc)
    {
        fVmHandler = new VMHandler(wrapperSvc);
        
        fLdapHandler = new LdapHandler(wrapperSvc);
        
        fSqlHandler = new SqlHandler(wrapperSvc);
        
        fMailHandler = new MailHandler(wrapperSvc);
        
        fServiceHandler = new ServiceHandler(wrapperSvc);
        
        WrapperConfig wrapperConf = wrapperSvc.getWrapperConfig();
        
        fScriptsDir = new File(wrapperConf.getfScriptsDirectory());
        
        fGenCertsScript = wrapperConf.getfScriptsDirectory()
            + wrapperConf.getfCertGenScript();
        
        //TODO: get states?
        
        fLogger = Logger.getLogger(this.getClass().getName());
    }
    
    public void initTenant(Tenant tenant) throws Exception
    {
        if(tenant.getState() != EntityState.NEW)
        {
            throw new Exception("tenant already initialized");
        }
        
        tenant.setState(EntityState.CREATING);
        TenantConfig config = tenant.getConfig();
        
        try
        {
            // TODO: create database user and databases
            fSqlHandler.initTenant(config);
            
            // TODO: create LDAP user and tree
            fLdapHandler.initTenant(config);
            
            // TODO: configure mail server
            fMailHandler.initTenant(config);
            
            // TODO: generate certificates
            String command = fGenCertsScript + " "
                + tenant.getConfig().getfTenantConsoleName();
            ProcessUtility.execute(command, fScriptsDir, fLogger);
            
            //TODO: create virtual machines?
            String tenantId = tenant.getConfig().getfTenantId();
            for(VMConfig vmc : config.getfVirtualMachines())
            {
                VirtualMachine vm = new VirtualMachine(vmc, tenantId);
                vm.setState(EntityState.NEW);
                fVmHandler.createVM(vm);

                // register in tenant
                tenant.getVMs().put(vmc.getfId(), vm);
            }
            
            //TODO: configure/spawn services using Mesos
            fServiceHandler.initTenant(config);
            
            //TODO: configure reverse proxy
            
        }
        catch(Exception e)
        {
            tenant.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        tenant.setState(EntityState.RUNNING);
    }
    
    public void updateTenant(Tenant tenant) throws Exception
    {
        tenant.setState(EntityState.MODIFYING);
        
        try
        {
            //TODO: compare configured state to current state
            
            //TODO: create or delete VMs

            //TODO: reconfigure reverse proxy
            
            //TODO: migrate services
        }
        catch(Exception e)
        {
            tenant.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        tenant.setState(EntityState.RUNNING);
    }
    
    public void newVM(VirtualMachine vm) throws Exception
    {
        // TODO: handle entirely through updateTenant?
        try
        {
            // TODO: initialize and start VM
            fVmHandler.createVM(vm);
            
            // TODO: start services
            fServiceHandler.newVM(vm);
            
            // TODO: reconfigure reverse proxy
        }
        catch(Exception e)
        {
            vm.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        vm.setState(EntityState.RUNNING);
    }
    
    public void deleteTenant(Tenant tenant) throws Exception
    {
        tenant.setState(EntityState.DELETING);
        
        TenantConfig config = tenant.getConfig();
        
        try
        {
            // TODO: reconfigure reverse rpoxy
            
            // TODO: kill services
            fServiceHandler.deleteTenant(config);
            
            // TODO: remove VMs
            fVmHandler.deleteTenant(config);
          
            // TODO: remove shared resources?

            // remove mail configuration
            fMailHandler.deleteTenant(config);
            
            // remove LDAP data
            fLdapHandler.deleteTenant(config);
            
            // remove databases
            fSqlHandler.deleteTenant(config);
            
            // TODO: remove certificates
        }
        catch(Exception e)
        {
            //TODO: try to delete as much as possible?
            tenant.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        tenant.setState(EntityState.DELETED);
    }
    
    public void deleteVM(VirtualMachine vm) throws Exception
    {
        // TODO: handle entirely through updateTenant?
        try
        {
            // TODO: delete services
            fServiceHandler.deleteVM(vm);
            
            // TODO: maybe wait a little to give mesos a chance to delete the services
            
            // TODO: stop and delete VM
            fVmHandler.deleteVM(vm);
            
            // TODO: relocate services?
            
            // TODO: reconfigure reverse proxy
        }
        catch(Exception e)
        {
            vm.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        vm.setState(EntityState.DELETED);
    }
}
