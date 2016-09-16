package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class ShindigConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.shindig.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.shindig.defaults.mem";
    
    private static final String DEF_MEM_MIN_PROP = "schub.services.shindig.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.shindig.defaults.java_mem_max";
    
    private static final String DEF_ES_PORT_PROP = "schub.services.shindig.defaults.es_port";
    
    private static final String DEF_WS_PORT_PROP = "schub.services.shindig.defaults.ws_port";
    private static final String DEF_WS_CONNS_PROP = "schub.services.shindig.defaults.ws_connections";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.SHINDIG_SERVICE);
        
        // TODO: generate security token here?

        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        String esHost = ServiceConstants.ELASTICSEARCH_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain();
        svcConf.put(ServiceConstants.SHINDIG_ES_HOST, esHost);
        svcConf.put(ServiceConstants.SHINDIG_ES_PORT, confs.getString(DEF_ES_PORT_PROP));
        String esClName = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH_SERVICE)
            .get(ServiceConstants.ELASTICSEARCH_CL_NAME);
        svcConf.put(ServiceConstants.SHINDIG_ES_CL_NAME, esClName);
        
        String wsServer = ServiceConstants.WEBSOCKET_SERVER_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain() + ":" + confs.getString(DEF_WS_PORT_PROP);
        svcConf.put(ServiceConstants.SHINDIG_WS_SERVER, wsServer);
        svcConf.put(ServiceConstants.SHINDIG_WS_CONNS, confs.getString(DEF_WS_CONNS_PROP));
    }

}
