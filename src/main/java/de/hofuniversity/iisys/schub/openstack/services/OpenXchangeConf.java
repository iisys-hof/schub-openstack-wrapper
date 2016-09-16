package de.hofuniversity.iisys.schub.openstack.services;

import java.util.Map;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.util.PasswordGenerator;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConfigs;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class OpenXchangeConf implements IServiceConfigurator
{
    private static final String DEF_CPUS_PROP = "schub.services.open-xchange.defaults.cpus";
    private static final String DEF_MEM_PROP = "schub.services.open-xchange.defaults.mem";

    private static final String DEF_MEM_MIN_PROP = "schub.services.open-xchange.defaults.java_mem_min";
    private static final String DEF_MEM_MAX_PROP = "schub.services.open-xchange.defaults.java_mem_max";

    private static final String DEF_SHIND_PORT_PROP = "schub.services.open-xchange.defaults.shindig_port";
    private static final String DEF_CAS_PORT_PROP = "schub.services.open-xchange.defaults.cas_port";
    
    @Override
    public void configure(TenantConfig tc, WrapperConfig wc, ClusterConfig cc)
        throws Exception
    {
        Map<String, String> svcConf = tc.getfServiceConfigs()
            .get(ServiceConstants.OPEN_XCHANGE_SERVICE);
        
        ServiceConfigs confs = ServiceConfigs.getInstance();
        
        svcConf.put(ServiceConstants.CPUS_PROP, confs.getString(DEF_CPUS_PROP));
        svcConf.put(ServiceConstants.MEM_PROP, confs.getString(DEF_MEM_PROP));

        svcConf.put(ServiceConstants.JAVA_MEM_MIN_PROP, confs.getString(DEF_MEM_MIN_PROP));
        svcConf.put(ServiceConstants.JAVA_MEM_MAX_PROP, confs.getString(DEF_MEM_MAX_PROP));
        
        svcConf.put(ServiceConstants.CERTS_PATH_PROP, cc.getfVmCertsPath());
        
        // TODO: URLs need to be external in the end
        String serverName = tc.getfTenantConsoleName() + ".schub.de";
        svcConf.put(ServiceConstants.OX_SERVER_NAME, serverName);
        
        // only set master password once
        String masterPw = svcConf.get(ServiceConstants.OX_MASTER_PASS);
        if(masterPw == null)
        {
            masterPw = new PasswordGenerator(wc.getfGenPasswordLength())
                .genPassword();
            svcConf.put(ServiceConstants.OX_MASTER_PASS, masterPw);
        }
        
        String appsuiteUrl = "https://" + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain() + "/appsuite/";
        svcConf.put(ServiceConstants.OX_APPSUITE_URL, appsuiteUrl);
        
        String shindigUrl = "https://" + ServiceConstants.SHINDIG_SERVICE
            + "." + cc.getfIntDomain()
            + ":" + confs.getString(DEF_SHIND_PORT_PROP) + "/shindig/";
        svcConf.put(ServiceConstants.OX_SHINDIG_URL, shindigUrl);
        
        // CAS
        String casUrl = "https://" + tc.getfTenantConsoleName()
            + "." + cc.getfIntDomain() + "/cas/";
        svcConf.put(ServiceConstants.OX_CAS_URL, casUrl);
        
        String clearPassUrl = "https://" + ServiceConstants.CAS_SERVICE
            + "." + cc.getfIntDomain()
            + ":" + confs.getString(DEF_CAS_PORT_PROP) + "/cas/clearPass";
        svcConf.put(ServiceConstants.OX_CAS_CLEARPASS_URL, clearPassUrl);
        
        String casAuthUrl = appsuiteUrl + "api/cascallback";
        svcConf.put(ServiceConstants.OX_CAS_AUTH_URL, casAuthUrl);
        
        String clearPassCallback = "https://" + ServiceConstants.OPEN_XCHANGE_SERVICE
            + "." + cc.getfIntDomain() + "/appsuite/api/cascallback";
        svcConf.put(ServiceConstants.OX_CLEARPASS_CALLBACK, clearPassCallback);
        
        String adminMail = "admin@" + tc.getfTenantConsoleName() + ".schub.de";
        svcConf.put(ServiceConstants.OX_ADMIN_MAIL, adminMail);
        
        // only set admin password once
        String adminPw = svcConf.get(ServiceConstants.OX_ADMIN_PASSWORD);
        if(adminPw == null)
        {
            adminPw = new PasswordGenerator(wc.getfGenPasswordLength())
                .genPassword();
            svcConf.put(ServiceConstants.OX_ADMIN_PASSWORD, adminPw);
        }
        
        String imapServer = cc.getfImapServer();
        svcConf.put(ServiceConstants.OX_IMAP_SERVER, imapServer);
        
        String smtpServer = cc.getfSmtpServer();
        svcConf.put(ServiceConstants.OX_SMTP_SERVER, smtpServer);
        
        String mailMasterPass = cc.getfImapMasterPassword();
        svcConf.put(ServiceConstants.OX_IMAP_MASTER_PW, mailMasterPass);
    }

}
