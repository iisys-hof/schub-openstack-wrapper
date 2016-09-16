package de.hofuniversity.iisys.schub.openstack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;

public class ConfigLoader
{
    private static final String PROPERTIES = "openstack-wrapper";

    private final Map<String, String> fProperties;
    
    public ConfigLoader()
    {
        fProperties = getProperties(PROPERTIES);
    }
    
    public WrapperConfig getWrapperConfig()
    {
        WrapperConfig config = new WrapperConfig();
        
        config.setfHomeDirectory(fProperties.get(WrapperConfig.HOME_DIR_PROP));
        config.setfTenantsDirectory(fProperties.get(WrapperConfig.TENANT_DIR_PROP));
        config.setfTemplateDirectory(fProperties.get(WrapperConfig.TEMPLATE_DIR_PROP));
        config.setfMarTempDirectory(fProperties.get(WrapperConfig.MAR_TEMP_DIR_PROP));
        config.setfTmpDirectory(fProperties.get(WrapperConfig.TMP_DIR_PROP));
        config.setfScriptsDirectory(fProperties.get(WrapperConfig.SCRIPTS_DIR_PROP));

        config.setfLogFile(fProperties.get(WrapperConfig.LOG_FILE_PROP));
        
        config.setfNetworkCidrTemp(fProperties.get(WrapperConfig.NET_TEMP_PROP));
        
        config.setfGenPasswordLength(Integer.parseInt(
            fProperties.get(WrapperConfig.PWD_LEN_PROP)));
        
        config.setfCertGenScript(fProperties.get(WrapperConfig.GEN_CERTS_SCRIPT_PROP));
        config.setfCertDeployScript(fProperties.get(WrapperConfig.DEPLOY_CERTS_SCRIPT_PROP));

        config.setfVmCreateScript(fProperties.get(WrapperConfig.VM_CREATE_SCRIPT_PROP));
        config.setfVmDeleteScript(fProperties.get(WrapperConfig.VM_DELETE_SCRIPT_PROP));
        config.setfVmGetIpScript(fProperties.get(WrapperConfig.VM_GET_IP_SCRIPT_PROP));
        config.setfVmHeatTemplate(fProperties.get(WrapperConfig.VM_TEMP_PROP));
        config.setfVmInitTemplate(fProperties.get(WrapperConfig.VM_INIT_PROP));
        
        config.setfSshKey(fProperties.get(WrapperConfig.SSH_KEY_PROP));
        config.setfSshUser(fProperties.get(WrapperConfig.SSH_USER_PROP));
        
        config.setfMonitoringInitWait(Long.parseLong(
            fProperties.get(WrapperConfig.MON_INIT_WAIT)));
        config.setfMonitoringVmPollInterval(Long.parseLong(
            fProperties.get(WrapperConfig.MON_VM_POLL_INT)));
        
        config.setfVmGetIpInterval(Long.parseLong(
            fProperties.get(WrapperConfig.VM_GET_IP_INT)));
        config.setfVmGetIpRetries(Integer.parseInt(
            fProperties.get(WrapperConfig.VM_GET_IP_RETRIES)));
        
        config.setfVmReadyCheckInterval(Long.parseLong(
            fProperties.get(WrapperConfig.VM_READY_INT)));
        config.setfVmReadyCheckRetries(Integer.parseInt(
            fProperties.get(WrapperConfig.VM_READY_RETRIES)));
        
        return config;
    }
    
    public ClusterConfig getClusterConfig()
    {
        ClusterConfig config = new ClusterConfig();
        
        config.setfAuthFile(fProperties.get(ClusterConfig.AUTH_FILE_PROP));
        
        config.setfIntDomain(fProperties.get(ClusterConfig.INT_DOMAIN_PROP));
        
        config.setfTenantImage(fProperties.get(ClusterConfig.TENANT_IMAGE_PROP));
        
        config.setfMaintenanceKey(fProperties.get(ClusterConfig.MAINTENANCE_KEY_PROP));
        
        config.setfNetworkId(fProperties.get(ClusterConfig.NET_ID_PROP));
        config.setfSubnetId(fProperties.get(ClusterConfig.SUBNET_ID_PROP));
        
        config.setfSecurityGroup(fProperties.get(ClusterConfig.SEC_GROUP_PROP));
        
        config.setfDockerRegistry(fProperties.get(ClusterConfig.DOCKER_REGISTRY_PROP));
        
        config.setfZookeeperHosts(fProperties.get(ClusterConfig.ZOO_HOSTS_PROP));
        
        config.setfConsulDatacenter(fProperties.get(ClusterConfig.CONSUL_DC_PROP));
        config.setfConsulMaster(fProperties.get(ClusterConfig.CONSUL_MASTER_PROP));
        
        config.setfLdapServer(fProperties.get(ClusterConfig.LDAP_SRV_PROP));
        config.setfLdapUser(fProperties.get(ClusterConfig.LDAP_USR_PROP));
        config.setfLdapPassword(fProperties.get(ClusterConfig.LDAP_PWD_PROP));
        
        String[] sqlHosts = fProperties.get(ClusterConfig.SQL_HOSTS_PROP).split(",");
        config.setfSqlHosts(sqlHosts);
        config.setfSqlPort(fProperties.get(ClusterConfig.SQL_PORT_PROP));
        config.setfSqlAdminHost(fProperties.get(ClusterConfig.SQL_ADMIN_HOST_PROP));
        config.setfSqlUser(fProperties.get(ClusterConfig.SQL_USR_PROP));
        config.setfSqlPassword(fProperties.get(ClusterConfig.SQL_PWD_PROP));
        
        String[] sqlLegacyHosts = fProperties.get(ClusterConfig.SQL_LEG_HOSTS_PROP).split(",");
        config.setfSqlLegacyHosts(sqlLegacyHosts);
        config.setfSqlLegacyPort(fProperties.get(ClusterConfig.SQL_LEG_PORT_PROP));
        
        config.setfMarathonUrl(fProperties.get(ClusterConfig.MRT_HOST_PROP));
        config.setfMarathonUser(fProperties.get(ClusterConfig.MRT_USR_PROP));
        
        config.setfReverseProxyHost(fProperties.get(ClusterConfig.RPROXY_HOST_PROP));
        config.setfReverseProxyUrl(fProperties.get(ClusterConfig.RPROXY_URL_PROP));
        config.setfReverseProxyUser(fProperties.get(ClusterConfig.RPROXY_USR_PROP));
        
        config.setfExtNetId(fProperties.get(ClusterConfig.EXT_NET_ID_PROP));
        config.setfExtNetName(fProperties.get(ClusterConfig.EXT_NET_NAME_PROP));
        config.setfExtNetCidr(fProperties.get(ClusterConfig.EXT_NET_CIDR_PROP));
        
        config.setfMailHost(fProperties.get(ClusterConfig.MAIL_HOST_PROP));
        config.setfImapServer(fProperties.get(ClusterConfig.IMAP_SERVER_PROP));
        config.setfSmtpServer(fProperties.get(ClusterConfig.SMTP_SERVER_PROP));
        config.setfImapMasterUser(fProperties.get(ClusterConfig.IMAP_MASTER_USER_PROP));
        config.setfImapMasterPassword(fProperties.get(ClusterConfig.IMAP_MASTER_PASS_PROP));
        
        config.setfVmCertsPath(fProperties.get(ClusterConfig.VM_CERTS_PATH));
        
        return config;
    }
    
    public Map<String, String> getProperties(String propsName)
    {
        Map<String, String> props = new HashMap<String, String>();
        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ResourceBundle rb = ResourceBundle.getBundle(propsName,
                Locale.getDefault(), loader);
        
        String key = null;
        String value = null;
        final Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements())
        {
            key = keys.nextElement();
            value = rb.getString(key);

            props.put(key, value);
        }
        
        return props;
    }
    
    public List<TenantConfig> getTenantConfigs()
    {
        List<TenantConfig> configs = new ArrayList<TenantConfig>();
        
        //list all json files in directory
        File directory = new File(fProperties.get(WrapperConfig.TENANT_DIR_PROP));
        
        if(directory != null
            && directory.isDirectory()
            && directory.listFiles() != null)
        {
            for(File file : directory.listFiles())
            {
                if(file.isFile())
                {
                    try
                    {
                        configs.add(readTenantConfig(file));
                    }
                    catch(Exception e)
                    {
                        //TODO: log
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            //TODO: exception or error message
            throw new RuntimeException("could not read from "
                + fProperties.get(WrapperConfig.TENANT_DIR_PROP)
                + "(" + directory + ")");
        }
        
        return configs;
    }
    
    private TenantConfig readTenantConfig(File file) throws Exception
    {
        //read JSON from file
        String source = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        String line = br.readLine();
        while(line != null)
        {
            source += line;
            
            line = br.readLine();
        }
        br.close();
        
        //read config from JSON
        return TenantConfigConverter.fromJSON(source);
    }
}
