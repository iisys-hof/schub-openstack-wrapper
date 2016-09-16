package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class CasConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.cas.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.cas.defaults.mem";
    
    private static final String DEF_MEM_MIN_PROP = "schub.services.cas.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.cas.defaults.java_mem_max";

    private static final String DEF_LR_PGT_CB_PROP = "schub.services.cas.defaults.liferay_pgt_callback";
    private static final String DEF_OX_PGT_CB_PROP = "schub.services.cas.defaults.ox_pgt_callback";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.CAS_SERVICE);
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        svcConf.put(ServiceConstants.CAS_ALLOWED_SERVICES, "");
        
        // TODO: this should be the actual external address in the end
        String serverName = "https://" + tc.getfTenantConsoleName() + "." + cc.getfIntDomain();
        svcConf.put(ServiceConstants.CAS_SERVER_NAME, serverName);
        
        String nodeName = "cas01." + tc.getfTenantConsoleName() + "." + cc.getfIntDomain();
        svcConf.put(ServiceConstants.CAS_NODE_NAME, nodeName);
        
        // TODO: these should be actual external addresses in the end
        // TODO: make more configurable
        String liferayCallback = serverName + confs.getString(DEF_LR_PGT_CB_PROP);
        svcConf.put(ServiceConstants.CAS_LIFERAY_CALLBACK, liferayCallback);
        
        String oxCallback = serverName + confs.getString(DEF_OX_PGT_CB_PROP);
        svcConf.put(ServiceConstants.CAS_OX_CALLBACK, oxCallback);
    }

}
