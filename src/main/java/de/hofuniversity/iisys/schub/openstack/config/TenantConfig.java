package de.hofuniversity.iisys.schub.openstack.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for a single tenant and all its general components.
 */
public class TenantConfig
{
    // directly contained properties
    public static final String ID_PROP = "id";
    public static final String NAME_PROP = "name";
    public static final String CON_NAME_PROP = "consoleName";
    public static final String MAIL_DOMAIN_PROP = "mailDomain";
    public static final String DESC_PROP = "description";

    public static final String NET_ID_PROP = "networkId";
    public static final String SUBNET_ID_PROP = "subnetId";
    public static final String NET_CIDR_PROP = "networkCidr";
    
    public static final String AVAIL_ZONE_PROP = "availabilityZone";
    
    public static final String VMS_PROP = "virtualMachines";

    public static final String SERVICE_CONFIGS_PROP = "service_configs";
    
    public static final String SEC_GROUP_PROP = "securityGroup";

    public static final String CONF_NAME_PROP = "name";
    public static final String CONF_VALUE_PROP = "value";
    public static final String CONF_PARAMS_PROP = "parameters";
    
    private String fTenantId;
    private String fTenantName;
    private String fTenantConsoleName;
    private String fTenantMailDomain;
    private String fTenantDescription;
    
    private String fNetworkId;
    private String fSubnetId;
    private String fNetworkCidr;
    
    private String fAvailZone;
    
    private String fSecurityGroup;
    
    private List<VMConfig> fVirtualMachines;
    
    private Map<String, Map<String, String>> fServiceConfigs;

    public TenantConfig()
    {
        fVirtualMachines = new ArrayList<>();
        fServiceConfigs = new HashMap<String, Map<String,String>>();
    }
    
    public String getfTenantId()
    {
        return fTenantId;
    }

    public void setfTenantId(String fTenantId)
    {
        this.fTenantId = fTenantId;
    }

    public String getfTenantName()
    {
        return fTenantName;
    }

    public void setfTenantName(String fTenantName)
    {
        this.fTenantName = fTenantName;
    }

    public String getfTenantConsoleName()
    {
        return fTenantConsoleName;
    }

    public void setfTenantConsoleName(String fTenantConsoleName)
    {
        this.fTenantConsoleName = fTenantConsoleName;
    }

    public String getfTenantMailDomain()
    {
        return fTenantMailDomain;
    }

    public void setfTenantMailDomain(String fTenantMailDomain)
    {
        this.fTenantMailDomain = fTenantMailDomain;
    }

    public String getfTenantDescription()
    {
        return fTenantDescription;
    }

    public void setfTenantDescription(String fTenantDescription)
    {
        this.fTenantDescription = fTenantDescription;
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

    public String getfNetworkCidr()
    {
        return fNetworkCidr;
    }

    public void setfNetworkCidr(String fNetworkCidr)
    {
        this.fNetworkCidr = fNetworkCidr;
    }

    public List<VMConfig> getfVirtualMachines()
    {
        return fVirtualMachines;
    }

    public void setfVirtualMachines(List<VMConfig> fVirtualMachines)
    {
        this.fVirtualMachines = fVirtualMachines;
    }

    public String getfAvailZone()
    {
        return fAvailZone;
    }

    public void setfAvailZone(String fAvailZone)
    {
        this.fAvailZone = fAvailZone;
    }

    public String getfSecurityGroup()
    {
        return fSecurityGroup;
    }

    public void setfSecurityGroup(String fSecurityGroup)
    {
        this.fSecurityGroup = fSecurityGroup;
    }

    public Map<String, Map<String, String>> getfServiceConfigs()
    {
        return fServiceConfigs;
    }

    public void setfServiceConfigs( Map<String, Map<String, String>> fServiceConfigs)
    {
        this.fServiceConfigs = fServiceConfigs;
    }
}
