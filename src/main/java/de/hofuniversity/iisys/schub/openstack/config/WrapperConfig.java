package de.hofuniversity.iisys.schub.openstack.config;

public class WrapperConfig
{
    public static final String HOME_DIR_PROP = "schub.openstack.wrapper.gome_directory";
    public static final String TENANT_DIR_PROP = "schub.openstack.wrapper.tenants_directory";
    public static final String TEMPLATE_DIR_PROP = "schub.openstack.wrapper.template_directory";
    public static final String MAR_TEMP_DIR_PROP = "schub.openstack.wrapper.marathon_template_directory";
    public static final String TMP_DIR_PROP = "schub.openstack.wrapper.tmp_directory";
    public static final String SCRIPTS_DIR_PROP = "schub.openstack.wrapper.scripts_directory";
    
    public static final String LOG_FILE_PROP = "schub.openstack.wrapper.log_file";
    
    public static final String NET_TEMP_PROP = "schub.openstack.wrapper.network_template";
    
    public static final String PWD_LEN_PROP = "schub.openstack.wrapper.gen_password_length";

    public static final String GEN_CERTS_SCRIPT_PROP = "schub.openstack.wrapper.cert_gen_script";
    public static final String DEPLOY_CERTS_SCRIPT_PROP = "schub.openstack.wrapper.cert_deploy_script";
    
    public static final String VM_CREATE_SCRIPT_PROP = "schub.openstack.wrapper.vm_create_script";
    public static final String VM_DELETE_SCRIPT_PROP = "schub.openstack.wrapper.vm_delete_script";
    public static final String VM_GET_IP_SCRIPT_PROP = "schub.openstack.wrapper.vm_get_ip_script";
    public static final String VM_TEMP_PROP = "schub.openstack.wrapper.vm_heat_template";
    public static final String VM_INIT_PROP = "schub.openstack.wrapper.vm_init_template";
    
    public static final String SSH_KEY_PROP = "schub.openstack.wrapper.ssh_key";
    public static final String SSH_USER_PROP = "schub.openstack.wrapper.ssh_user";
    
    public static final String MON_INIT_WAIT = "schub.openstack.wrapper.monitoring.init_wait";
    public static final String MON_VM_POLL_INT = "schub.openstack.wrapper.monitoring.vm_poll_int";
    
    public static final String VM_GET_IP_INT = "schub.openstack.wrapper.vms.get_ip_int";
    public static final String VM_GET_IP_RETRIES = "schub.openstack.wrapper.vms.get_ip_retries";
    
    public static final String VM_READY_INT = "schub.openstack.wrapper.vms.ready_check_int";
    public static final String VM_READY_RETRIES = "schub.openstack.wrapper.vms.ready_check_retries";
    
    private String fHomeDirectory;
    private String fTenantsDirectory;
    private String fTemplateDirectory;
    private String fMarTempDirectory;
    private String fTmpDirectory;
    private String fScriptsDirectory;
    
    private String fLogFile;
    
    private String fNetworkCidrTemp;
    
    private int fGenPasswordLength;

    private String fCertGenScript;
    private String fCertDeployScript;
    
    private String fVmCreateScript;
    private String fVmDeleteScript;
    private String fVmGetIpScript;
    private String fVmHeatTemplate;
    private String fVmInitTemplate;
    
    private String fSshKey;
    private String fSshUser;
    
    private long fMonitoringInitWait;
    private long fMonitoringVmPollInterval;
    
    private long fVmGetIpInterval;
    private int fVmGetIpRetries;
    
    private long fVmReadyCheckInterval;
    private int fVmReadyCheckRetries;

    public String getfHomeDirectory()
    {
        return fHomeDirectory;
    }

    public void setfHomeDirectory(String fHomeDirectory)
    {
        this.fHomeDirectory = fHomeDirectory;
    }

    public String getfTenantsDirectory()
    {
        return fTenantsDirectory;
    }

    public void setfTenantsDirectory(String fTenantsDirectory)
    {
        this.fTenantsDirectory = fTenantsDirectory;
    }

    public String getfTemplateDirectory()
    {
        return fTemplateDirectory;
    }

    public void setfTemplateDirectory(String fTemplateDirectory)
    {
        this.fTemplateDirectory = fTemplateDirectory;
    }

    public String getfMarTempDirectory()
    {
        return fMarTempDirectory;
    }

    public void setfMarTempDirectory(String fMarTempDirectory)
    {
        this.fMarTempDirectory = fMarTempDirectory;
    }

    public String getfTmpDirectory()
    {
        return fTmpDirectory;
    }

    public void setfTmpDirectory(String fTmpDirectory)
    {
        this.fTmpDirectory = fTmpDirectory;
    }

    public String getfScriptsDirectory()
    {
        return fScriptsDirectory;
    }

    public void setfScriptsDirectory(String fScriptsDirectory)
    {
        this.fScriptsDirectory = fScriptsDirectory;
    }

    public String getfLogFile()
    {
        return fLogFile;
    }

    public void setfLogFile(String fLogFile)
    {
        this.fLogFile = fLogFile;
    }

    public String getfNetworkCidrTemp()
    {
        return fNetworkCidrTemp;
    }

    public void setfNetworkCidrTemp(String fNetworkCidrTemp)
    {
        this.fNetworkCidrTemp = fNetworkCidrTemp;
    }

    public int getfGenPasswordLength()
    {
        return fGenPasswordLength;
    }

    public void setfGenPasswordLength(int fGenPasswordLength)
    {
        this.fGenPasswordLength = fGenPasswordLength;
    }

    public String getfCertGenScript()
    {
        return fCertGenScript;
    }

    public void setfCertGenScript(String fCertGenScript)
    {
        this.fCertGenScript = fCertGenScript;
    }

    public String getfCertDeployScript()
    {
        return fCertDeployScript;
    }

    public void setfCertDeployScript(String fCertDeployScript)
    {
        this.fCertDeployScript = fCertDeployScript;
    }

    public String getfVmCreateScript()
    {
        return fVmCreateScript;
    }

    public void setfVmCreateScript(String fVmCreateScript)
    {
        this.fVmCreateScript = fVmCreateScript;
    }

    public String getfVmDeleteScript()
    {
        return fVmDeleteScript;
    }

    public void setfVmDeleteScript(String fVmDeleteScript)
    {
        this.fVmDeleteScript = fVmDeleteScript;
    }

    public String getfVmGetIpScript()
    {
        return fVmGetIpScript;
    }

    public void setfVmGetIpScript(String fVmGetIpScript)
    {
        this.fVmGetIpScript = fVmGetIpScript;
    }

    public String getfVmHeatTemplate()
    {
        return fVmHeatTemplate;
    }

    public void setfVmHeatTemplate(String fVmHeatTemplate)
    {
        this.fVmHeatTemplate = fVmHeatTemplate;
    }

    public String getfVmInitTemplate()
    {
        return fVmInitTemplate;
    }

    public void setfVmInitTemplate(String fVmInitTemplate)
    {
        this.fVmInitTemplate = fVmInitTemplate;
    }

    public String getfSshKey()
    {
        return fSshKey;
    }

    public void setfSshKey(String fSshKey)
    {
        this.fSshKey = fSshKey;
    }

    public String getfSshUser()
    {
        return fSshUser;
    }

    public void setfSshUser(String fSshUser)
    {
        this.fSshUser = fSshUser;
    }

    public long getfMonitoringInitWait()
    {
        return fMonitoringInitWait;
    }

    public void setfMonitoringInitWait(long fMonitoringInitWait)
    {
        this.fMonitoringInitWait = fMonitoringInitWait;
    }

    public long getfMonitoringVmPollInterval()
    {
        return fMonitoringVmPollInterval;
    }

    public void setfMonitoringVmPollInterval(long fMonitoringVmPollInterval)
    {
        this.fMonitoringVmPollInterval = fMonitoringVmPollInterval;
    }

    public long getfVmGetIpInterval()
    {
        return fVmGetIpInterval;
    }

    public void setfVmGetIpInterval(long fVmGetIpInterval)
    {
        this.fVmGetIpInterval = fVmGetIpInterval;
    }

    public int getfVmGetIpRetries()
    {
        return fVmGetIpRetries;
    }

    public void setfVmGetIpRetries(int fVmGetIpRetries)
    {
        this.fVmGetIpRetries = fVmGetIpRetries;
    }

    public long getfVmReadyCheckInterval()
    {
        return fVmReadyCheckInterval;
    }

    public void setfVmReadyCheckInterval(long fVmReadyCheckInterval)
    {
        this.fVmReadyCheckInterval = fVmReadyCheckInterval;
    }

    public int getfVmReadyCheckRetries()
    {
        return fVmReadyCheckRetries;
    }

    public void setfVmReadyCheckRetries(int fVmReadyCheckRetries)
    {
        this.fVmReadyCheckRetries = fVmReadyCheckRetries;
    }
}
