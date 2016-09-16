package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class CamundaConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP="schub.services.camunda.defaults.cpus";
    private static final String DEF_MEM_PROP="schub.services.camunda.defaults.mem";
    
    private static final String DEF_MEM_MIN_PROP="schub.services.camunda.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP="schub.services.camunda.defaults.java_mem_max";
    
    private static final String DEF_ADMIN_PROP="schub.services.camunda.defaults.admin";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.CAMUNDA_SERVICE);
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        svcConf.put(ServiceConstants.CAMUNDA_DEFAULT_ADMIN, confs.getString(DEF_ADMIN_PROP));
        
        // TODO: these should be external addresses in the end
        String serverName = "https://" + tc.getfTenantConsoleName() + "." + cc.getfIntDomain();
        String casLoginUrl = serverName + "/cas/login";
        String camundaUrl = serverName + "/camunda/";
        
        // CAS verify URL (internal)
        String casUrl = "https://" + ServiceConstants.CAS_SERVICE
            + "." + tc.getfTenantConsoleName() + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT + "/cas/";
        
        svcConf.put(ServiceConstants.CAMUNDA_CAS_LOGIN_URL, casLoginUrl);
        svcConf.put(ServiceConstants.CAMUNDA_CAS_SERVER_URL, casUrl);
        svcConf.put(ServiceConstants.CAMUNDA_SERVER_NAME, serverName);
        svcConf.put(ServiceConstants.CAMUNDA_URL, camundaUrl);
        
        String shindigUrl = "https://" + ServiceConstants.SHINDIG_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT + "/shindig/";
        svcConf.put(ServiceConstants.CAMUNDA_SHINDIG_URL, shindigUrl);
    }

}
