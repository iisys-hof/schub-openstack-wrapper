package de.hofuniversity.iisys.schub.openstack.model;

import de.hofuniversity.iisys.schub.openstack.config.VMConfig;

/**
 * Runtime model class for a virtual machine including its transient state.
 */
public class VirtualMachine
{
    public static final String TENANT_ID_PROP = "tenantId";
    
    private VMConfig fConfig;
    
    private String fTenantId;
    
    private EntityState fState;
    
    private String fStatusMessage;
    
    public VirtualMachine(VMConfig config, String tenantId)
    {
        fConfig = config;
        
        fTenantId = tenantId;
        
        fState = EntityState.UNKNOWN;
    }
    
    public VMConfig getConfig()
    {
        return fConfig;
    }
    
    public void setConfig(VMConfig config)
    {
        fConfig = config;
    }
    
    public EntityState getState()
    {
        return fState;
    }
    
    public void setState(EntityState state)
    {
        fState = state;
    }
    
    public String getStatusMessage()
    {
        return fStatusMessage;
    }
    
    public void setStatusMessage(String statusMessage)
    {
        fStatusMessage = statusMessage;
    }
    
    public String getTenantId()
    {
        return fTenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        fTenantId = tenantId;
    }
}
