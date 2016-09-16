package de.hofuniversity.iisys.schub.openstack.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the configuration of a single VM for a tenant.
 * The overall network configuration is taken from the tenant's configuration.
 */
public class VMConfig
{
    public static final String ID_PROP = "id";
    public static final String NAME_PROP = "name";

    public static final String IMAGE_NAME_PROP = "imageName";

    public static final String FLAVOR_NAME_PROP = "flavorName";

    public static final String IP_ADDRS_PROP = "ipAddresses";

    public static final String SERVICES_PROP = "services";
    
    public static final String VIRT_CPUS_PROP = "virt_cpus";
    public static final String VIRT_RAM_PROP = "virt_ram";
    public static final String VIRT_DISK_PROP = "virt_disk";

    private String fId;
    private String fName;

    private String fImageName;

    private String fFlavorName;
    
    private String fWeaveHostIp;
    private String fWeaveHostNet;

    private List<String> fIpAddresses;

    private List<String> fServices;
    
    private String fVirtCPUs;
    private String fVirtRAM;
    private String fVirtDisk;
    
    public VMConfig()
    {
        fIpAddresses = new ArrayList<>();
        fServices = new ArrayList<>();
    }
    
    public String getfId()
    {
        return fId;
    }
    
    public void setfId(String fId)
    {
        this.fId = fId;
    }
    
    public String getfName()
    {
        return fName;
    }
    
    public void setfName(String fName)
    {
        this.fName = fName;
    }
    
    public String getfImageName()
    {
        return fImageName;
    }
    
    public void setfImageName(String fImageName)
    {
        this.fImageName = fImageName;
    }
    
    public String getfFlavorName()
    {
        return fFlavorName;
    }
    
    public void setfFlavorName(String fFlavorName)
    {
        this.fFlavorName = fFlavorName;
    }
    
    public String getfWeaveHostIp()
    {
        return fWeaveHostIp;
    }

    public void setfWeaveHostIp(String fWeaveHostIp)
    {
        this.fWeaveHostIp = fWeaveHostIp;
    }

    public String getfWeaveHostNet()
    {
        return fWeaveHostNet;
    }

    public void setfWeaveHostNet(String fWeaveHostNet)
    {
        this.fWeaveHostNet = fWeaveHostNet;
    }

    public List<String> getfIpAddresses()
    {
        return fIpAddresses;
    }
    
    public void setfIpAddresses(List<String> fIpAddresses)
    {
        this.fIpAddresses = fIpAddresses;
    }
    
    public List<String> getfServices()
    {
        return fServices;
    }
    
    public void setfServices(List<String> fServices)
    {
        this.fServices = fServices;
    }

    public String getfVirtCPUs()
    {
        return fVirtCPUs;
    }

    public void setfVirtCPUs(String fVirtCPUs)
    {
        this.fVirtCPUs = fVirtCPUs;
    }

    public String getfVirtRAM()
    {
        return fVirtRAM;
    }

    public void setfVirtRAM(String fVirtRAM)
    {
        this.fVirtRAM = fVirtRAM;
    }

    public String getfVirtDisk()
    {
        return fVirtDisk;
    }

    public void setfVirtDisk(String fVirtDisk)
    {
        this.fVirtDisk = fVirtDisk;
    }
}
