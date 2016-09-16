package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.PasswordGenerator;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class LiferayConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.liferay.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.liferay.defaults.mem";

    private static final String DEF_MEM_MIN_PROP = "schub.services.liferay.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.liferay.defaults.java_mem_max";

    private static final String DEF_ES_PORT_PROP = "schub.services.liferay.defaults.es_port";

    private static final String DEF_ADMIN_ID_PROP = "schub.services.liferay.defaults.admin_id";
    private static final String DEF_ADMIN_MAIL_PROP = "schub.services.liferay.defaults.admin_mail";
    private static final String DEF_ADMIN_FULL_NAME_PROP = "schub.services.liferay.defaults.admin_full_name";
    private static final String DEF_ADMIN_FIRST_NAME_PROP = "schub.services.liferay.defaults.admin_first_name";
    private static final String DEF_ADMIN_LAST_NAME_PROP = "schub.services.liferay.defaults.admin_last_name";

    private static final String DEF_NX_USER_PROP = "schub.services.liferay.defaults.nuxeo_user";
    private static final String DEF_NX_PASSWORD_PROP = "schub.services.liferay.defaults.nuxeo_password";

    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.LIFERAY_SERVICE);

        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        // admin data
        svcConf.put(ServiceConstants.LIFERAY_ADMIN_ID,
            confs.getString(DEF_ADMIN_ID_PROP));
        svcConf.put(ServiceConstants.LIFERAY_ADMIN_MAIL,
            confs.getString(DEF_ADMIN_MAIL_PROP));
        svcConf.put(ServiceConstants.LIFERAY_ADMIN_FULL_NAME,
            confs.getString(DEF_ADMIN_FULL_NAME_PROP));
        svcConf.put(ServiceConstants.LIFERAY_ADMIN_FIRST_NAME,
            confs.getString(DEF_ADMIN_FIRST_NAME_PROP));
        svcConf.put(ServiceConstants.LIFERAY_ADMIN_LAST_NAME,
            confs.getString(DEF_ADMIN_LAST_NAME_PROP));
        
        // only generate admin password once
        String adminPass = svcConf.get(ServiceConstants.LIFERAY_ADMIN_PASSWORD);
        if(adminPass == null)
        {
            adminPass = new PasswordGenerator(wc.getfGenPasswordLength())
                .genPassword();
            svcConf.put(ServiceConstants.LIFERAY_ADMIN_PASSWORD, adminPass);
        }
        
        
        
        svcConf.put(ServiceConstants.LIFERAY_COMPANY_NAME, tc.getfTenantName());
        
        // TODO: put tenant's own site here?
        String webId = tc.getfTenantConsoleName() + ".schub.de";
        svcConf.put(ServiceConstants.LIFERAY_COMPANY_WEBID, webId);

        // CAS
        // TODO: these should (mostly) be external URLs in the end
        String serverName = "https://" + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain();
        String loginUrl = serverName + "/cas/login";
        String logoutUrl = serverName + "/cas/login";
        String casUrl = serverName + "/cas";
        String clearpassUrl = serverName + "/cas/clearPass";
        String liferayLoginUrl = serverName + "/c/portal/login";
        String pgtCallback = serverName + "/CASClearPass/pgtCallback";
        
        svcConf.put(ServiceConstants.LIFERAY_CAS_LOGIN_URL, loginUrl);
        svcConf.put(ServiceConstants.LIFERAY_CAS_LOGOUT_URL, logoutUrl);
        svcConf.put(ServiceConstants.LIFERAY_CAS_SERVER_URL, casUrl);
        svcConf.put(ServiceConstants.LIFERAY_CAS_SERVER_NAME, serverName);
        svcConf.put(ServiceConstants.LIFERAY_CAS_CLEARPASS_URL, clearpassUrl);
        svcConf.put(ServiceConstants.LIFERAY_LOGIN_URL, liferayLoginUrl);
        svcConf.put(ServiceConstants.LIFERAY_PGT_CALLBACK_URL, pgtCallback);
        svcConf.put(ServiceConstants.LIFERAY_URL, serverName);
        
        
        // TODO: this shouldn't be an admin user since it's for guests
        svcConf.put(ServiceConstants.LIFERAY_CMIS_USER,
            confs.getString(DEF_NX_USER_PROP));
        svcConf.put(ServiceConstants.LIFERAY_CMIS_PASSWORD,
            confs.getString(DEF_NX_PASSWORD_PROP));
        
        // TODO: does the internal URL suffice? only works with port
        String nuxeoUrl = "https://" + ServiceConstants.NUXEO_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + ServiceConstants.TOMCAT_HTTPS_PORT;
        svcConf.put(ServiceConstants.LIFERAY_NUXEO_URL, nuxeoUrl);
        
        String esServer = ServiceConstants.ELASTICSEARCH2_SERVICE
            + "." + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain()
            + ":" + confs.getString(DEF_ES_PORT_PROP);
        svcConf.put(ServiceConstants.LIFERAY_ES_SERVER, esServer);
        
        String esClName = tc.getfServiceConfigs()
            .get(ServiceConstants.ELASTICSEARCH2_SERVICE)
            .get(ServiceConstants.ELASTICSEARCH_CL_NAME);
        svcConf.put(ServiceConstants.LIFERAY_ES_CLUSTER_NAME, esClName);
        
        String shindigUrl = serverName + "/shindig/";
        svcConf.put(ServiceConstants.LIFERAY_SHINDIG_URL, shindigUrl);
        
        String shindigToken = tc.getfServiceConfigs()
            .get(ServiceConstants.SHINDIG_SERVICE)
            .get(ServiceConstants.SHINDIG_SEC_TOKEN);
        svcConf.put(ServiceConstants.LIFERAY_SHINDIG_TOKEN, shindigToken);
        
        String skillWikiUrl = serverName + "/web/guest/wiki/-/wiki/Main";
        svcConf.put(ServiceConstants.LIFERAY_SKILL_WIKI_URL, skillWikiUrl);
        
        String linkUrl = serverName + "/c";
        svcConf.put(ServiceConstants.LIFERAY_LINK_URL, linkUrl);
    }

}
