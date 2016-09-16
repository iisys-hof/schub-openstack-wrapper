package de.hofuniversity.iisys.schub.openstack.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;

public class TenantConfigConverter
{
    private static final Logger fLogger =
        Logger.getLogger(TenantConfigConverter.class.getName());
    
    public static JSONObject toJSON(TenantConfig config) throws Exception
    {
        JSONObject json = new JSONObject();
        
        json.put(TenantConfig.ID_PROP, config.getfTenantId());
        json.put(TenantConfig.NAME_PROP, config.getfTenantName());
        json.put(TenantConfig.CON_NAME_PROP, config.getfTenantConsoleName());
        json.put(TenantConfig.MAIL_DOMAIN_PROP, config.getfTenantMailDomain());
        json.put(TenantConfig.DESC_PROP, config.getfTenantDescription());

        json.put(TenantConfig.NET_ID_PROP, config.getfNetworkId());
        json.put(TenantConfig.SUBNET_ID_PROP, config.getfSubnetId());
        
        json.put(TenantConfig.NET_CIDR_PROP, config.getfNetworkCidr());

        json.put(TenantConfig.AVAIL_ZONE_PROP, config.getfAvailZone());
        
        json.put(TenantConfig.SEC_GROUP_PROP, config.getfSecurityGroup());
        
        JSONArray vmArray = new JSONArray();
        for(VMConfig vmc : config.getfVirtualMachines())
        {
            vmArray.put(toJSON(vmc));
        }
        json.put(TenantConfig.VMS_PROP, vmArray);
        
        JSONArray confArr = new JSONArray();
        for(Entry<String, Map<String, String>> confE : config.getfServiceConfigs().entrySet())
        {
            JSONObject confObj = new JSONObject();
            confObj.put(VMConfig.NAME_PROP, confE.getKey());
            
            JSONArray paramArr = new JSONArray();
            for(Entry<String, String> paramE : confE.getValue().entrySet())
            {
                JSONObject paramObj = new JSONObject();
                paramObj.put(TenantConfig.CONF_NAME_PROP, paramE.getKey());
                paramObj.put(TenantConfig.CONF_VALUE_PROP, paramE.getValue());
                paramArr.put(paramObj);
            }
            confObj.put(TenantConfig.CONF_PARAMS_PROP, paramArr);
            
            confArr.put(confObj);
        }
        json.put(TenantConfig.SERVICE_CONFIGS_PROP, confArr);
        
        return json;
    }
    
    public static JSONObject toJSON(VMConfig config) throws Exception
    {
        JSONObject json = new JSONObject();

        json.put(VMConfig.ID_PROP, config.getfId());
        json.put(VMConfig.NAME_PROP, config.getfName());

        json.put(VMConfig.IMAGE_NAME_PROP, config.getfImageName());

        json.put(VMConfig.FLAVOR_NAME_PROP, config.getfFlavorName());
        
        json.put(ServiceConstants.WEAVE_HOST_IP, config.getfWeaveHostIp());
        json.put(ServiceConstants.WEAVE_HOST_NETWORK, config.getfWeaveHostNet());
        
        JSONArray ipArr = new JSONArray();
        for(String ipAdd : config.getfIpAddresses())
        {
            ipArr.put(ipAdd);
        }
        json.put(VMConfig.IP_ADDRS_PROP, ipArr);

        JSONArray svcArr = new JSONArray();
        for(String svc : config.getfServices())
        {
            svcArr.put(svc);
        }
        json.put(VMConfig.SERVICES_PROP, svcArr);
        
        json.put(VMConfig.VIRT_CPUS_PROP, config.getfVirtCPUs());
        json.put(VMConfig.VIRT_RAM_PROP, config.getfVirtRAM());
        json.put(VMConfig.VIRT_DISK_PROP, config.getfVirtDisk());
        
        return json;
    }
    
    public static TenantConfig fromJSON(String source) throws Exception
    {
        TenantConfig config = new TenantConfig();
        
        JSONObject json = new JSONObject(source);
        
        config.setfTenantId(json.optString(TenantConfig.ID_PROP));
        config.setfTenantName(json.optString(TenantConfig.NAME_PROP));
        config.setfTenantConsoleName(json.optString(TenantConfig.CON_NAME_PROP));
        config.setfTenantMailDomain(json.optString(TenantConfig.MAIL_DOMAIN_PROP));
        config.setfTenantDescription(json.optString(TenantConfig.DESC_PROP));

        config.setfNetworkId(json.optString(TenantConfig.NET_ID_PROP));
        config.setfSubnetId(json.optString(TenantConfig.SUBNET_ID_PROP));
        
        config.setfNetworkCidr(json.optString(TenantConfig.NET_CIDR_PROP));

        config.setfAvailZone(json.optString(TenantConfig.AVAIL_ZONE_PROP));
        
        config.setfSecurityGroup(json.optString(TenantConfig.SEC_GROUP_PROP));
        
        //read virtual machine definitions
        JSONArray vmArray = json.optJSONArray(TenantConfig.VMS_PROP);
        List<VMConfig> vmConfigs = new ArrayList<VMConfig>();
        
        if(vmArray != null)
        {
            for(int i = 0; i < vmArray.length(); ++i)
            {
                vmConfigs.add(readVmConfig(vmArray.getJSONObject(i)));
            }
        }
        
        config.setfVirtualMachines(vmConfigs);

        Map<String, Map<String, String>> serviceConfigs = new HashMap<String, Map<String, String>>();
        
        JSONArray svcConfsArr = json.optJSONArray(TenantConfig.SERVICE_CONFIGS_PROP);
        if(svcConfsArr != null)
        {
            for(int i = 0; i < svcConfsArr.length(); ++i)
            {
                Map<String, String> serviceConfig = new HashMap<String, String>();
                
                JSONObject svcConf = svcConfsArr.getJSONObject(i);
                
                JSONArray paramsArr = svcConf.optJSONArray(TenantConfig.CONF_PARAMS_PROP);
                if(paramsArr != null)
                {
                    for(int j = 0; j < paramsArr.length(); ++j)
                    {
                        JSONObject param = paramsArr.getJSONObject(j);
                        
                        String name = param.optString(TenantConfig.CONF_NAME_PROP);
                        String value = param.optString(TenantConfig.CONF_VALUE_PROP);
                        
                        serviceConfig.put(name, value);
                    }
                }
                
                if(svcConf.has(TenantConfig.CONF_NAME_PROP))
                {
                    serviceConfigs.put(svcConf.getString(
                        TenantConfig.CONF_NAME_PROP), serviceConfig);
                }
                else
                {
                    fLogger.log(Level.WARNING, "unnamed service configuration");
                }
            }
        }
        
        config.setfServiceConfigs(serviceConfigs);
        
        return config;
    }
    
    public static VMConfig readVmConfig(JSONObject object) throws Exception
    {
        VMConfig config = new VMConfig();
        
        config.setfId(object.optString(VMConfig.ID_PROP));
        config.setfName(object.optString(VMConfig.NAME_PROP));

        config.setfImageName(object.optString(VMConfig.IMAGE_NAME_PROP));

        config.setfFlavorName(object.optString(VMConfig.FLAVOR_NAME_PROP));
        
        config.setfWeaveHostIp(object.optString(ServiceConstants.WEAVE_HOST_IP));
        config.setfWeaveHostNet(object.optString(ServiceConstants.WEAVE_HOST_NETWORK));
        
        JSONArray ipArr = object.optJSONArray(VMConfig.IP_ADDRS_PROP);
        if(ipArr != null)
        {
            List<String> ips = new ArrayList<String>();
            
            for(int i = 0; i < ipArr.length(); ++i)
            {
                ips.add(ipArr.getString(i));
            }
            
            config.setfIpAddresses(ips);
        }

        JSONArray svcArr = object.optJSONArray(VMConfig.SERVICES_PROP);
        if(svcArr != null)
        {
            List<String> services = new ArrayList<String>();
            
            for(int i = 0; i < svcArr.length(); ++i)
            {
                services.add(svcArr.getString(i));
            }
            
            config.setfServices(services);
        }
        
        config.setfVirtCPUs(object.optString(VMConfig.VIRT_CPUS_PROP));
        config.setfVirtRAM(object.optString(VMConfig.VIRT_RAM_PROP));
        config.setfVirtDisk(object.optString(VMConfig.VIRT_DISK_PROP));
        
        return config;
    }
    
    public static JSONArray toJSON(Map<String, String> source) throws Exception
    {
        JSONArray array = new JSONArray();
        
        for(Entry<String, String> mapE : source.entrySet())
        {
            JSONObject entry = new JSONObject();
            
            entry.put(TenantConfig.CONF_NAME_PROP, mapE.getKey());
            entry.put(TenantConfig.CONF_VALUE_PROP, mapE.getValue());
            
            array.put(entry);
        }
        
        return array;
    }
    
    public static Map<String, String> readServiceConf(JSONArray source) throws Exception
    {
        Map<String, String> svcConf = new HashMap<String, String>();
        
        for(int i = 0; i < source.length(); ++i)
        {
            JSONObject entry = source.getJSONObject(i);
            
            String name = entry.getString(TenantConfig.CONF_NAME_PROP);
            String value = entry.getString(TenantConfig.CONF_VALUE_PROP);
            
            svcConf.put(name, value);
        }
        
        return svcConf;
    }
}
