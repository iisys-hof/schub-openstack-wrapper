package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class NginxConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.nginx.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.nginx.defaults.mem";
    
    private static final String DEF_WEAVE_DNS_PROP = "schub.services.nginx.defaults.weave_dns";
    
    private static final String DEF_MAX_REQ_SIZE_PROP = "schub.services.nginx.defaults.max_req_size";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.NGINX_SERVICE);
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        svcConf.put(ServiceConstants.NGINX_TENANT_NAME, tc.getfTenantConsoleName());
        
        svcConf.put(ServiceConstants.NGINX_LOCAL_DOMAIN, cc.getfIntDomain());
        
        svcConf.put(ServiceConstants.NGINX_WEAVE_DNS, confs.getString(DEF_WEAVE_DNS_PROP));
        
        svcConf.put(ServiceConstants.NGINX_MAX_REQUEST_SIZE, confs.getString(DEF_MAX_REQ_SIZE_PROP));
    }

}
