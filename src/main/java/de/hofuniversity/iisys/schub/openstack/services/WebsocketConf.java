package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class WebsocketConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.websocket-server.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.websocket-server.defaults.mem";

    private static final String DEF_THREADS_PROP = "schub.services.websocket-server.defaults.threads";

    private static final String DEF_MEM_MIN_PROP = "schub.services.websocket-server.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.websocket-server.defaults.java_mem_max";
    
    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.WEBSOCKET_SERVER_SERVICE);
        
        svcConf.put(ServiceConstants.WEBSOCKET_ORG_NAME, tc.getfTenantName());
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));
        
        svcConf.put(ServiceConstants.WEBSOCKET_THREADS, confs.getString(DEF_THREADS_PROP));
        
        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        // fields must be set in frontend
        svcConf.put(ServiceConstants.WEBSOCKET_ORG_FIELD, "");
        svcConf.put(ServiceConstants.WEBSOCKET_ORG_SUBFIELD, "");
        svcConf.put(ServiceConstants.WEBSOCKET_ORG_WEBPAGE, "");
    }
}
