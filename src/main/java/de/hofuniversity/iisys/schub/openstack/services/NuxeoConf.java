package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class NuxeoConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.nuxeo.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.nuxeo.defaults.mem";

    private static final String DEF_MEM_MIN_PROP = "schub.services.nuxeo.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.nuxeo.defaults.java_mem_max";

    private static final String DEF_ES_PORT_PROP = "schub.services.nuxeo.defaults.es_port";
    
    private static final String DEF_SHIND_PORT_PROP = "schub.services.nuxeo.defaults.shindig_port";
    
    private static final String DEF_ACT_LANG_PROP = "schub.services.nuxeo.defaults.activity_lang";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.NUXEO_SERVICE);
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        String esServer = ServiceConstants.ELASTICSEARCH_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + confs.getString(DEF_ES_PORT_PROP);
        svcConf.put(ServiceConstants.NUXEO_ES_SERVER, esServer);
        
        String esClName = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH_SERVICE)
            .get(ServiceConstants.ELASTICSEARCH_CL_NAME);
        svcConf.put(ServiceConstants.NUXEO_ES_CLUSTER, esClName);
        
        // CAS
        // TODO: these should (mostly) be external URLs in the end
        String serverName = "https://" + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain();
        String casServerName = "https://" + ServiceConstants.CAS_SERVICE
            + "." + tc.getfTenantConsoleName() + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT;
        
        String appUrl = serverName + "/nuxeo/nxstartup.faces";
        svcConf.put(ServiceConstants.NUXEO_CAS_APP_URL, appUrl);
        
        String loginUrl = serverName + "/cas/login";
        svcConf.put(ServiceConstants.NUXEO_CAS_LOGIN_URL, loginUrl);
        
        // NOT an external URL
        String validateUrl = casServerName + "/cas/serviceValidate";
        svcConf.put(ServiceConstants.NUXEO_CAS_VAL_URL, validateUrl);

        String logoutUrl = serverName + "/cas/login";
        svcConf.put(ServiceConstants.NUXEO_CAS_LOGOUT_URL, logoutUrl);
        
        
        String nuxeoUrl = serverName + "/nuxeo/";
        svcConf.put(ServiceConstants.NUXEO_URL, nuxeoUrl);
        
        String shindigUrl = "https://" + ServiceConstants.SHINDIG_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + confs.getString(DEF_SHIND_PORT_PROP) + "/shindig/";
        svcConf.put(ServiceConstants.NUXEO_SHINDIG_URL, shindigUrl);
        
        svcConf.put(ServiceConstants.NUXEO_ACTIVITY_LANG,
            confs.getString(DEF_ACT_LANG_PROP));
        
        String profileUrl = serverName + "/web/guest/profile?userId=";
        svcConf.put(ServiceConstants.NUXEO_PROFILE_URL, profileUrl);
        
        
        // Camunda
        // TODO: these should (mostly) be external URLs in the end
        String camundaUrl = serverName + "/camunda/";
        svcConf.put(ServiceConstants.NUXEO_CAMUNDA_URL, camundaUrl);
        
        String camundaRestUrl = serverName + "/engine-rest/";
        svcConf.put(ServiceConstants.NUXEO_CAMUNDA_REST_URL, camundaRestUrl);
        
        String jsUtilsUrl = serverName + "/bpmn-js-utils";
        svcConf.put(ServiceConstants.NUXEO_BPMN_JS_UTILS_URL, jsUtilsUrl);
        
        // TODO: won't work this way
        svcConf.put(ServiceConstants.NUXEO_PROCESS_BASE_URL, serverName);
    }

}
