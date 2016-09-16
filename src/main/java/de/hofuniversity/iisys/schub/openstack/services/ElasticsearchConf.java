package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class ElasticsearchConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.elasticsearch.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.elasticsearch.defaults.mem";
    private static final String DEF_HEAP_PROP = "schub.services.elasticsearch.defaults.heap_size";

    private static final String DEF_CPUS_PROP2 = "schub.services.elasticsearch2.defaults.cpus";
    private static final String DEF_MEM_PROP2 = "schub.services.elasticsearch2.defaults.mem";
    private static final String DEF_HEAP_PROP2 = "schub.services.elasticsearch2.defaults.heap_size";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        String tenantName = tc.getfTenantConsoleName();
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        // configure both Elasticsearch 1.x and 2.x
        // cluster name is already configured by config generator
        // ES 1.x
        Map<String, String> esConf = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH_SERVICE);
        
        esConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        esConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));
        esConf.put(ServiceConstants.ELASTICSEARCH_HEAP_SIZE, confs.getString(DEF_HEAP_PROP));
        
        String nodeName = "tenant-" + tenantName + "-es1";
        esConf.put(ServiceConstants.ELASTICSEARCH_NODE_NAME, nodeName);
        
        // ES 2.x
        esConf = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH2_SERVICE);

        esConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP2));
        esConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP2));
        esConf.put(ServiceConstants.ELASTICSEARCH_HEAP_SIZE, confs.getString(DEF_HEAP_PROP2));
        
        nodeName = "tenant-" + tenantName + "-es2";
        esConf.put(ServiceConstants.ELASTICSEARCH_NODE_NAME, nodeName);
    }

}
