package de.hofuniversity.iisys.schub.openstack.services;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;

public interface IServiceConfigurator
{
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception;
}
