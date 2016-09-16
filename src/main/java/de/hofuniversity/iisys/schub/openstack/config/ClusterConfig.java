package de.hofuniversity.iisys.schub.openstack.config;

/**
 * Configuration defining general and shared parameters of an OpenStack
 * cluster and related cluster systems.
 */
public class ClusterConfig
{
    public static final String AUTH_FILE_PROP = "schub.openstack.cluster.auth_file";
    
    public static final String INT_DOMAIN_PROP = "schub.openstack.cluster.internal_domain";
    
    public static final String TENANT_IMAGE_PROP = "schub.openstack.cluster.tenant_vm_image";
    
    public static final String MAINTENANCE_KEY_PROP = "schub.openstack.cluster.ssh_key";
    
    public static final String NET_ID_PROP = "schub.openstack.cluster.network_id";
    public static final String SUBNET_ID_PROP = "schub.openstack.cluster.subnet_id";
    
    public static final String SEC_GROUP_PROP = "schub.openstack.cluster.security_group";
    
    public static final String DOCKER_REGISTRY_PROP = "schub.openstack.cluster.docker_registry";
    
    public static final String ZOO_HOSTS_PROP = "schub.openstack.cluster.zookeeper_hosts";
    
    public static final String CONSUL_DC_PROP = "schub.openstack.cluster.consul_datacenter";
    public static final String CONSUL_MASTER_PROP = "schub.openstack.cluster.consul_master";
    
    public static final String LDAP_SRV_PROP = "schub.openstack.cluster.ldap_uri";
    public static final String LDAP_USR_PROP = "schub.openstack.cluster.ldap_user";
    public static final String LDAP_PWD_PROP = "schub.openstack.cluster.ldap_password";

    public static final String SQL_HOSTS_PROP = "schub.openstack.cluster.sql_hosts";
    public static final String SQL_PORT_PROP = "schub.openstack.cluster.sql_port";
    public static final String SQL_ADMIN_HOST_PROP = "schub.openstack.cluster.sql_admin_host";
    public static final String SQL_USR_PROP = "schub.openstack.cluster.sql_user";
    public static final String SQL_PWD_PROP = "schub.openstack.cluster.sql_password";
    
    public static final String SQL_LEG_HOSTS_PROP = "schub.openstack.cluster.sql.legacy_hosts";
    public static final String SQL_LEG_PORT_PROP = "schub.openstack.cluster.sql.legacy_port";

    public static final String MRT_HOST_PROP = "schub.openstack.cluster.marathon_url";
    public static final String MRT_USR_PROP = "schub.openstack.cluster.marathon_user";

    public static final String RPROXY_HOST_PROP = "schub.openstack.cluster.reverse_proxy_host";
    public static final String RPROXY_URL_PROP = "schub.openstack.cluster.reverse_proxy_url";
    public static final String RPROXY_USR_PROP = "schub.openstack.cluster.reverse_proxy_user";
    
    public static final String EXT_NET_ID_PROP = "schub.openstack.cluster.external_network_id";
    public static final String EXT_NET_NAME_PROP = "schub.openstack.cluster.external_network_name";
    public static final String EXT_NET_CIDR_PROP = "schub.openstack.cluster.external_network_cidr";
    
    public static final String MAIL_HOST_PROP = "schub.openstack.cluster.mail.host";
    public static final String IMAP_SERVER_PROP = "schub.openstack.cluster.imap.server";
    public static final String SMTP_SERVER_PROP = "schub.openstack.cluster.smtp.server";
    public static final String IMAP_MASTER_USER_PROP = "schub.openstack.cluster.imap.master_user";
    public static final String IMAP_MASTER_PASS_PROP = "schub.openstack.cluster.imap.master_password";
    
    public static final String VM_CERTS_PATH = "schub.openstack.cluster.vm.certs_path";
    
    //contains tenant name, user name, password and authentication URL
    private String fAuthFile;
    
    private String fIntDomain;
    
    private String fTenantImage;
    
    private String fMaintenanceKey;
    
    private String fNetworkId;
    private String fSubnetId;
    
    private String fSecurityGroup;
    
    private String fDockerRegistry;
    
    private String fZookeeperHosts;

    private String fConsulDatacenter;
    private String fConsulMaster;
    
    private String fLdapServer;
    private String fLdapUser;
    private String fLdapPassword;
    
    private String[] fSqlHosts;
    private String fSqlPort;
    private String fSqlAdminHost;
    private String fSqlUser;
    private String fSqlPassword;
    
    private String[] fSqlLegacyHosts;
    private String fSqlLegacyPort;

    private String fMarathonUrl;
    private String fMarathonUser;

    private String fReverseProxyHost;
    private String fReverseProxyUrl;
    private String fReverseProxyUser;
    
    private String fExtNetId;
    private String fExtNetName;
    private String fExtNetCidr;
    
    private String fMailHost;
    private String fImapServer;
    private String fSmtpServer;
    private String fImapMasterUser;
    private String fImapMasterPassword;
    
    private String fVmCertsPath;
    
    public String getfAuthFile()
    {
        return fAuthFile;
    }
    
    public void setfAuthFile(String fAuthFile)
    {
        this.fAuthFile = fAuthFile;
    }
    
    public String getfIntDomain()
    {
        return fIntDomain;
    }

    public void setfIntDomain(String fIntDomain)
    {
        this.fIntDomain = fIntDomain;
    }

    public String getfTenantImage()
    {
        return fTenantImage;
    }

    public void setfTenantImage(String fTenantImage)
    {
        this.fTenantImage = fTenantImage;
    }

    public String getfMaintenanceKey()
    {
        return fMaintenanceKey;
    }

    public void setfMaintenanceKey(String fMaintenanceKey)
    {
        this.fMaintenanceKey = fMaintenanceKey;
    }

    public String getfNetworkId()
    {
        return fNetworkId;
    }

    public void setfNetworkId(String fNetworkId)
    {
        this.fNetworkId = fNetworkId;
    }

    public String getfSubnetId()
    {
        return fSubnetId;
    }

    public void setfSubnetId(String fSubnetId)
    {
        this.fSubnetId = fSubnetId;
    }

    public String getfSecurityGroup()
    {
        return fSecurityGroup;
    }

    public void setfSecurityGroup(String fSecurityGroup)
    {
        this.fSecurityGroup = fSecurityGroup;
    }

    public String getfDockerRegistry()
    {
        return fDockerRegistry;
    }

    public void setfDockerRegistry(String fDockerRegistry)
    {
        this.fDockerRegistry = fDockerRegistry;
    }

    public String getfZookeeperHosts()
    {
        return fZookeeperHosts;
    }

    public void setfZookeeperHosts(String fZookeeperHosts)
    {
        this.fZookeeperHosts = fZookeeperHosts;
    }

    public String getfConsulDatacenter()
    {
        return fConsulDatacenter;
    }

    public void setfConsulDatacenter(String fConsulDatacenter)
    {
        this.fConsulDatacenter = fConsulDatacenter;
    }

    public String getfConsulMaster()
    {
        return fConsulMaster;
    }

    public void setfConsulMaster(String fConsulMaster)
    {
        this.fConsulMaster = fConsulMaster;
    }

    public String getfLdapServer()
    {
        return fLdapServer;
    }
    
    public void setfLdapServer(String fLdapServer)
    {
        this.fLdapServer = fLdapServer;
    }
    
    public String getfLdapUser()
    {
        return fLdapUser;
    }
    
    public void setfLdapUser(String fLdapUser)
    {
        this.fLdapUser = fLdapUser;
    }
    
    public String getfLdapPassword()
    {
        return fLdapPassword;
    }
    
    public void setfLdapPassword(String fLdapPassword)
    {
        this.fLdapPassword = fLdapPassword;
    }
    
    public String[] getfSqlHosts()
    {
        return fSqlHosts;
    }
    
    public void setfSqlHosts(String[] fSqlHosts)
    {
        this.fSqlHosts = fSqlHosts;
    }
    
    public String getfSqlPort()
    {
        return fSqlPort;
    }

    public void setfSqlPort(String fSqlPort)
    {
        this.fSqlPort = fSqlPort;
    }

    public String getfSqlAdminHost()
    {
        return fSqlAdminHost;
    }

    public void setfSqlAdminHost(String fSqlAdminHost)
    {
        this.fSqlAdminHost = fSqlAdminHost;
    }

    public String getfSqlUser()
    {
        return fSqlUser;
    }
    
    public void setfSqlUser(String fSqlUser)
    {
        this.fSqlUser = fSqlUser;
    }
    
    public String getfSqlPassword()
    {
        return fSqlPassword;
    }
    
    public void setfSqlPassword(String fSqlPassword)
    {
        this.fSqlPassword = fSqlPassword;
    }
    
    public String[] getfSqlLegacyHosts()
    {
        return fSqlLegacyHosts;
    }
    
    public void setfSqlLegacyHosts(String[] fSqlLegacyHosts)
    {
        this.fSqlLegacyHosts = fSqlLegacyHosts;
    }
    
    public String getfSqlLegacyPort()
    {
        return fSqlLegacyPort;
    }

    public void setfSqlLegacyPort(String fSqlLegacyPort)
    {
        this.fSqlLegacyPort = fSqlLegacyPort;
    }
    
    public String getfMarathonUrl()
    {
        return fMarathonUrl;
    }

    public void setfMarathonUrl(String fMarathonHost)
    {
        this.fMarathonUrl = fMarathonHost;
    }

    public String getfMarathonUser()
    {
        return fMarathonUser;
    }

    public void setfMarathonUser(String fMarathonUser)
    {
        this.fMarathonUser = fMarathonUser;
    }

    public String getfReverseProxyHost()
    {
        return fReverseProxyHost;
    }
    
    public void setfReverseProxyHost(String fReverseProxyHost)
    {
        this.fReverseProxyHost = fReverseProxyHost;
    }
    
    public String getfReverseProxyUrl()
    {
        return fReverseProxyUrl;
    }
    
    public void setfReverseProxyUrl(String fReverseProxyUrl)
    {
        this.fReverseProxyUrl = fReverseProxyUrl;
    }
    
    public String getfReverseProxyUser()
    {
        return fReverseProxyUser;
    }

    public void setfReverseProxyUser(String fReverseProxyUser)
    {
        this.fReverseProxyUser = fReverseProxyUser;
    }

    public String getfExtNetId()
    {
        return fExtNetId;
    }
    
    public void setfExtNetId(String fExtNetId)
    {
        this.fExtNetId = fExtNetId;
    }
    
    public String getfExtNetName()
    {
        return fExtNetName;
    }
    
    public void setfExtNetName(String fExtNetName)
    {
        this.fExtNetName = fExtNetName;
    }
    
    public String getfExtNetCidr()
    {
        return fExtNetCidr;
    }
    
    public void setfExtNetCidr(String fExtNetCidr)
    {
        this.fExtNetCidr = fExtNetCidr;
    }

    public String getfMailHost()
    {
        return fMailHost;
    }

    public void setfMailHost(String fMailHost)
    {
        this.fMailHost = fMailHost;
    }

    public String getfImapServer()
    {
        return fImapServer;
    }

    public void setfImapServer(String fImapServer)
    {
        this.fImapServer = fImapServer;
    }

    public String getfSmtpServer()
    {
        return fSmtpServer;
    }

    public void setfSmtpServer(String fSmtpServer)
    {
        this.fSmtpServer = fSmtpServer;
    }

    public String getfImapMasterUser()
    {
        return fImapMasterUser;
    }

    public void setfImapMasterUser(String fImapMasterUser)
    {
        this.fImapMasterUser = fImapMasterUser;
    }

    public String getfImapMasterPassword()
    {
        return fImapMasterPassword;
    }

    public void setfImapMasterPassword(String fImapMasterPassword)
    {
        this.fImapMasterPassword = fImapMasterPassword;
    }

    public String getfVmCertsPath()
    {
        return fVmCertsPath;
    }

    public void setfVmCertsPath(String fVmCertsPath)
    {
        this.fVmCertsPath = fVmCertsPath;
    }
}
