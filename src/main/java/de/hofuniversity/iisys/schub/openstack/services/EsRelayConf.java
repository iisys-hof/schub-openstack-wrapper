package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class EsRelayConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.es-relay.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.es-relay.defaults.mem";
    
    private static final String DEF_MEM_MIN_PROP = "schub.services.es-relay.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.es-relay.defaults.java_mem_max";

    private static final String DEF_LR_INDEX_PROP = "schub.services.es-relay.defaults.liferay_index";
    private static final String DEF_LR_COMP_ID_PROP = "schub.services.es-relay.defaults.liferay_company_id";
    private static final String DEF_LR_PASS_ROLES_PROP = "schub.services.es-relay.defaults.liferay_pass_roles";

    private static final String DEF_NX_USER_PROP = "schub.services.es-relay.defaults.nuxeo_user";
    private static final String DEF_NX_PASSWORD_PROP = "schub.services.es-relay.defaults.nuxeo_password";
    
    private static final String ES_HTTP_PORT = "9200";
    private static final String ES_TRANSPORT_PORT = "9300";
    
    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.ES_RELAY_SERVICE);

        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        // elasticsearch 1.x
        String esHost = ServiceConstants.ELASTICSEARCH_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain();
        svcConf.put(ServiceConstants.ES_RELAY_ES_HOST, esHost);
        
        String esUrl = "http://" + esHost + ":" + ES_HTTP_PORT + "/";
        svcConf.put(ServiceConstants.ES_RELAY_ES_URL, esUrl);
        
        svcConf.put(ServiceConstants.ES_RELAY_ES_PORT, ES_TRANSPORT_PORT);
        
        String esClName = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH_SERVICE)
            .get(ServiceConstants.ELASTICSEARCH_CL_NAME);
        svcConf.put(ServiceConstants.ES_RELAY_ES_CLUSTER_NAME, esClName);
        
        // elasticsearch 2.x
        String es2Host = ServiceConstants.ELASTICSEARCH2_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain();
        svcConf.put(ServiceConstants.ES_RELAY_ES2_HOST, es2Host);
        
        String es2Url = "http://" + es2Host + ":" + ES_HTTP_PORT + "/";
        svcConf.put(ServiceConstants.ES_RELAY_ES2_URL, es2Url);
        
        svcConf.put(ServiceConstants.ES_RELAY_ES2_PORT, ES_TRANSPORT_PORT);

        String es2ClName = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH2_SERVICE)
            .get(ServiceConstants.ELASTICSEARCH_CL_NAME);
        svcConf.put(ServiceConstants.ES_RELAY_ES2_CLUSTER_NAME, es2ClName);
        
        // Liferay
        svcConf.put(ServiceConstants.ES_RELAY_LR_INDEX, confs.getString(DEF_LR_INDEX_PROP));
        
        String liferayUrl = "https://" + ServiceConstants.LIFERAY_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT + "/";
        svcConf.put(ServiceConstants.ES_RELAY_LR_URL, liferayUrl);

        String liferayCompId = confs.getString(DEF_LR_COMP_ID_PROP);
        svcConf.put(ServiceConstants.ES_RELAY_LR_COMP_ID, liferayCompId);
        
        Map<String, String> lrSvcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.LIFERAY_SERVICE);
        String liferayUser = lrSvcConf.get(ServiceConstants.LIFERAY_ADMIN_ID);
        svcConf.put(ServiceConstants.ES_RELAY_LR_USER, liferayUser);
        
        String liferayPass = lrSvcConf.get(ServiceConstants.LIFERAY_ADMIN_PASSWORD);
        svcConf.put(ServiceConstants.ES_RELAY_LR_PASS, liferayPass);

        svcConf.put(ServiceConstants.ES_RELAY_LR_PASS_ROLES, confs.getString(DEF_LR_PASS_ROLES_PROP));
        
        // Nuxeo
        // TODO: does the internal URL suffice? only works with port
        String nuxeoUrl = "https://" + ServiceConstants.NUXEO_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT
            + "/nuxeo/";
        svcConf.put(ServiceConstants.ES_RELAY_NX_URL, nuxeoUrl);
        
        String nuxeoUser = confs.getString(DEF_NX_USER_PROP);
        svcConf.put(ServiceConstants.ES_RELAY_NX_USER, nuxeoUser);
        
        String nuxeoPass = confs.getString(DEF_NX_PASSWORD_PROP);
        svcConf.put(ServiceConstants.ES_RELAY_NX_PASS, nuxeoPass);
        
        // Shindig
        String shindigUrl = "https://" + ServiceConstants.SHINDIG_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT + "/shindig/";
        svcConf.put(ServiceConstants.CAMUNDA_SHINDIG_URL, shindigUrl);
        
        // CAS
        // TODO: these should be external addresses in the end
        String serverName = "https://" + tc.getfTenantConsoleName() + "." + cc.getfIntDomain();
        String casLoginUrl = serverName + "/cas/login";
        
        // CAS verify URL (internal)
        String casUrl = "https://" + ServiceConstants.CAS_SERVICE
            + "." + tc.getfTenantConsoleName() + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT + "/cas/";
        
        svcConf.put(ServiceConstants.ES_RELAY_CAS_LOGIN_URL, casLoginUrl);
        svcConf.put(ServiceConstants.ES_RELAY_CAS_URL, casUrl);
        svcConf.put(ServiceConstants.ES_RELAY_CAS_SRV_NAME, serverName);
    }

}
