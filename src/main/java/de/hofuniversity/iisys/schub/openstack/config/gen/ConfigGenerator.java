package de.hofuniversity.iisys.schub.openstack.config.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.model.Tenant;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.util.PasswordGenerator;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

public class ConfigGenerator
{
    private final WrapperService fWrapperService;
    private final WrapperConfig fWrapperConf;
    private final ClusterConfig fClusterConf;
    
    private final String fIntDomain;
    
    private final String fImageName;
    
    private final PasswordGenerator fPassGen;
    
    public ConfigGenerator()
    {
        fWrapperService = WrapperService.getInstance();
        fWrapperConf = fWrapperService.getWrapperConfig();
        fClusterConf = fWrapperService.getClusterConfig();
        
        fIntDomain = fClusterConf.getfIntDomain();
        
        fImageName = fClusterConf.getfTenantImage();
        
        fPassGen = new PasswordGenerator(fWrapperConf.getfGenPasswordLength());
    }
    
    public Tenant initTenant(TenantConfig tc) throws Exception
    {
        // TODO: check if name or console name are already used
        
        // add generated values
        tc.setfTenantId(getNewId(true));
        
        String consoleName = tc.getfTenantConsoleName();
        
        //TODO: check values for usability
        if(tc.getfTenantName() == null
            || tc.getfTenantName().isEmpty())
        {
            throw new Exception("no tenant name");
        }
        if(consoleName == null
            || consoleName.isEmpty())
        {
            throw new Exception("no tenant console name");
        }
        
        // set network CIDR
        String cidr = fWrapperConf.getfNetworkCidrTemp();
        cidr = cidr.replace('x', '0').replace('y', '0') + "/16";
        tc.setfNetworkCidr(cidr);
        
        // set network IDs
        tc.setfNetworkId(fClusterConf.getfNetworkId());
        tc.setfSubnetId(fClusterConf.getfSubnetId());
        
        // set security group
        tc.setfSecurityGroup(fClusterConf.getfSecurityGroup());
        
        // initialize service configurations
        Map<String, Map<String, String>> serviceConfigs = tc.getfServiceConfigs();
        for(String service : ServiceConstants.SERVICES)
        {
            //TODO: tenant-specific names and other settings from base configuration?
            Map<String, String> svcConf = new HashMap<String, String>();
            
            // set tenant name
            svcConf.put(ServiceConstants.SERVICE_TENANT_PROP, consoleName);
            
            // store generated service IDs
            String svcId = service + "-" + consoleName;
            svcConf.put(ServiceConstants.SERVICE_ID_PROP, svcId);
            
            // store generated hostnames
            String hostname = service + "." + consoleName + "." + fIntDomain;
            svcConf.put(ServiceConstants.HOSTNAME_PROP, hostname);
            
            serviceConfigs.put(service, svcConf);
        }
        
        // create weave configuration
        Map<String, String> weaveConf = new HashMap<String, String>();
        
        // generate weave network settings and password
        weaveConf.put(ServiceConstants.WEAVE_PEERS, "");
        weaveConf.put(ServiceConstants.WEAVE_PASSWORD, fPassGen.genPassword());
        
        // general network
        weaveConf.put(ServiceConstants.WEAVE_NETWORK, cidr);
        
        // host IP template
        String hostIp = fWrapperConf.getfNetworkCidrTemp();
        hostIp = hostIp.replace("x", "255") + "/16";
        weaveConf.put(ServiceConstants.WEAVE_HOST_IP, hostIp);
        
        // per-host network template
        String hostNet = fWrapperConf.getfNetworkCidrTemp();
        hostNet = hostNet.replace('y', '0') + "/24";
        weaveConf.put(ServiceConstants.WEAVE_HOST_NETWORK, hostNet);
        
        serviceConfigs.put(ServiceConstants.WEAVE_SERVICE, weaveConf);
        
        // create shared Shindig security token
        // TODO: not recommended - does this even work?
        String secToken = new PasswordGenerator(43).genPassword() + "=";
        
        serviceConfigs.get(ServiceConstants.SHINDIG_SERVICE)
            .put(ServiceConstants.SHINDIG_SEC_TOKEN, secToken);
        
        // create shared Elasticsearch cluster names
        // TODO: base on configurable template
        String esCluster = "tenant-" + consoleName + "-cluster";
        String es2Cluster = "tenant-" + consoleName + "-cluster2";
        
        serviceConfigs.get(ServiceConstants.ELASTICSEARCH_SERVICE)
            .put(ServiceConstants.ELASTICSEARCH_CL_NAME, esCluster);
        serviceConfigs.get(ServiceConstants.ELASTICSEARCH2_SERVICE)
            .put(ServiceConstants.ELASTICSEARCH_CL_NAME, es2Cluster);
        
        return new Tenant(tc);
    }
    
    public VirtualMachine initVM(VMConfig vmc, String tenantId) throws Exception
    {
        vmc.setfId(getNewId(false));
        
        // globally used tenant VM image
        vmc.setfImageName(fImageName);
        
        VirtualMachine vm = new VirtualMachine(vmc, tenantId);
        
        //TODO check values for usability
        
        return vm;
    }
    
    private String getNewId(boolean tenant)
    {
        // TODO: numeric IDs / console names as IDs?
        // TODO: locking for thread safety?
        String id = null;
        
        while(id == null)
        {
            //double check that ID is still available
            id = UUID.randomUUID().toString();
            
            if(tenant)
            {
                //check tenants
                if(fWrapperService.getTenants().get(id) != null)
                {
                    //ID already used, reroll
                    id = null;
                }
            }
            else
            {
                //check virutal machines
                if(fWrapperService.getVMs().get(id) != null)
                {
                    //ID already used, reroll
                    id = null;
                }
            }
        }
        
        return id;
    }
}
