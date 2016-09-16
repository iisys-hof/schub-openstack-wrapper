package de.hofuniversity.iisys.schub.openstack.model;

import java.util.HashMap;
import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;

public class Tenant
{
    private final Map<String, VirtualMachine> fVMs;
    
    private TenantConfig fConfig;
    
    private EntityState fState;
    
    private String fStatusMessage;
    
    public Tenant(TenantConfig config)
    {
        fConfig = config;
        
        fVMs = new HashMap<String, VirtualMachine>();
        
        fState = EntityState.UNKNOWN;
    }
    
    public Map<String, VirtualMachine> getVMs()
    {
        return fVMs;
    }
    
    public TenantConfig getConfig()
    {
        return fConfig;
    }
    
    public void setConfig(TenantConfig config)
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
}
