package de.hofuniversity.iisys.schub.openstack.processing;

import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;

/**
 * Handles creation and modification of tenant networks and routers.
 * 
 * Not needed at the moment - shared networks and routers are used via heat.
 */
public class NetworkHandler
{
    public NetworkHandler()
    {
        
    }
    
    public void initTenant(TenantConfig config)
    {
        //TODO: create dedicated tenant network
        
        //TODO: create router to external network
    }
    
    public void deleteTenant(TenantConfig config)
    {
        //TODO: delete networks and routers
    }
}
