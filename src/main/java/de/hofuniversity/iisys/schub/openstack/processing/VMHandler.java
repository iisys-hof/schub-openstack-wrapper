package de.hofuniversity.iisys.schub.openstack.processing;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.ClusterConfig;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;
import de.hofuniversity.iisys.schub.openstack.model.EntityState;
import de.hofuniversity.iisys.schub.openstack.model.VirtualMachine;
import de.hofuniversity.iisys.schub.openstack.util.ProcessUtility;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;
import de.hofuniversity.iisys.schub.openstack.util.TemplateProcessor;

/**
 * Handles creation and modification of tenant VMs.
 */
public class VMHandler
{
    /** 
     * sequence of tenant VM names since numbers can't be used
     * access by number/index
     * caution when deleting VMs in the middle of the sequence
     */
    public static final String[] VM_NUMS = {"one","two","three","four","five",
        "six","seven","eight","nine","ten"};
    
    public static final String SERVER_NAME_PROP = "INSERT_SERVER_NAME_HERE";
    public static final String IMAGE_NAME_PROP = "INSERT_IMAGE_NAME_HERE";
    public static final String FLAVOR_PROP = "INSERT_FLAVOR_HERE";
    public static final String AVAIL_ZONE_PROP = "INSERT_AVAILABILITY_ZONE_HERE";
    public static final String SSH_KEY_PROP = "INSERT_SSH_KEY_NAME_HERE";
    public static final String INIT_SCRIPT_PROP = "INSERT_INIT_SCRIPT_FILE_HERE";
    public static final String NETWORK_ID_PROP = "INSERT_NETWORK_ID_HERE";
    public static final String NETWORK_SUBNET_PROP = "INSERT_NETWORK_SUBNET_HERE";
    public static final String SEC_GROUP_PROP = "INSERT_SECURITY_GROUP_HERE";
    
    public static final String REV_PROXY_PROP = "INSERT_REVERSE_PROXY_ACTIVE_HERE";
    public static final String D_REGISTRY_PROP = "INSERT_DOCKER_REGISTRY_HERE";
    public static final String TENANT_NAME_PROP = "INSERT_TENANT_NAME_HERE";
    public static final String VM_NAME_PROP = "INSERT_TENANT_VM_NAME_HERE";
    public static final String ZK_SERVERS_PROP = "INSERT_ZOOKEEPER_SERVERS_HERE";
    public static final String WEAVE_PEERS_PROP = "INSERT_WEAVE_PEERS_HERE";
    public static final String WEAVE_PASSWORD_PROP = "INSERT_WEAVE_PASSWORD_HERE";
    public static final String WEAVE_HOST_ADDR_PROP = "INSERT_WEAVE_HOST_ADDRESS_HERE";
    public static final String WEAVE_NET_PROP = "INSERT_WEAVE_NETWORK_HERE";
    public static final String WEAVE_VM_NET_PROP = "INSERT_WEAVE_VM_NETWORK_HERE";
    public static final String PROMETHEUS_PROP = "INSERT_PROMETHEUS_ACTIVE_HERE";
    public static final String CONSUL_DTC_PROP = "INSERT_CONSUL_DATACENTER_HERE";
    public static final String CONSUL_PEER_PROP = "INSERT_CONSUL_PEER_HERE";
    public static final String MESOS_CORES_PROP = "INSERT_MESOS_CPU_CORES_HERE";
    public static final String MESOS_RAM_PROP = "INSERT_MESOS_RAM_MVS_HERE";
    public static final String MESOS_DISK_PROP = "INSERT_MESOS_DISK_MBS_HERE";
    
    private static final int MAX_NET = 253;
    
    private final WrapperService fWrapperService;
    
    private final WrapperConfig fWrapperConfig;
    private final ClusterConfig fClusterConfig;
    
    private final String fTmpDirectory;
    private final String fScriptsDirectory;
    
    private final String fOSCredentials;
    
    private final String fDeployCertsScript;
    
    private final String fSshKeyFile;
    private final String fSshUser;
    
    // values for cloud init
    private final String fDockerRegistry;
    
    private final String fConsulDatacenter;
    private final String fConsulMaster;
    
    // values for heat
    private final String fVmCreateScript;
    private final String fVmDeleteScript;
    private final String fVmGetIpScript;
    private final String fVmHeatTemplate;
    private final String fVmInitTemplate;
    
    private final String fSshKey;
    
    private final String fVmNetwork;
    private final String fVmSubnet;
    
    private final String fSecGroup;
    
    private final long fGetIpInt;
    private final int fGetIpRetries;
    
    private final long fReadyCheckInt;
    private final int fReadyCheckRetries;
    
    private final Logger fLogger;
    
    public VMHandler(WrapperService wrapperSvc)
    {
        fWrapperService = wrapperSvc;
        
        fWrapperConfig = wrapperSvc.getWrapperConfig();
        fClusterConfig = wrapperSvc.getClusterConfig();
        
        fTmpDirectory = fWrapperConfig.getfTmpDirectory();
        fScriptsDirectory = fWrapperConfig.getfScriptsDirectory();
        
        fOSCredentials = fClusterConfig.getfAuthFile();
        
        fDeployCertsScript = fScriptsDirectory
            + fWrapperConfig.getfCertDeployScript();
        
        fSshKeyFile = fWrapperConfig.getfSshKey();
        fSshUser = fWrapperConfig.getfSshUser();
        
        String tempDir = fWrapperConfig.getfTemplateDirectory();
        fVmCreateScript = tempDir + fWrapperConfig.getfVmCreateScript();
        fVmDeleteScript = tempDir + fWrapperConfig.getfVmDeleteScript();
        fVmGetIpScript = tempDir + fWrapperConfig.getfVmGetIpScript();
        fVmHeatTemplate = tempDir + fWrapperConfig.getfVmHeatTemplate();
        fVmInitTemplate = tempDir + fWrapperConfig.getfVmInitTemplate();
        
        fDockerRegistry = fClusterConfig.getfDockerRegistry();
        
        fConsulDatacenter = fClusterConfig.getfConsulDatacenter();
        fConsulMaster = fClusterConfig.getfConsulMaster();
        
        fSshKey = fClusterConfig.getfMaintenanceKey();
        
        fVmNetwork = fClusterConfig.getfNetworkId();
        fVmSubnet = fClusterConfig.getfSubnetId();
        
        fSecGroup = fClusterConfig.getfSecurityGroup();
        
        fGetIpInt = fWrapperConfig.getfVmReadyCheckInterval();
        fGetIpRetries = fWrapperConfig.getfVmGetIpRetries();
        
        fReadyCheckInt = fWrapperConfig.getfVmReadyCheckInterval();
        fReadyCheckRetries = fWrapperConfig.getfVmReadyCheckRetries();
        
        fLogger = Logger.getLogger(this.getClass().getName());
    }

    private String getVmIp(String stackName) throws Exception
    {
        String command = fVmGetIpScript + " " + fOSCredentials
            + " " + stackName;
        
        return ProcessUtility.getOuput(command, new File(fTmpDirectory));
    }
    
    public void createVM(VirtualMachine vm) throws Exception
    {
        if(vm.getState() != EntityState.NEW)
        {
            throw new Exception("virtual machine already created");
        }
        
        vm.setState(EntityState.CREATING);
        VMConfig vmc = vm.getConfig();
        
        TenantConfig tc = fWrapperService.getTenants()
            .get(vm.getTenantId()).getConfig();
        
        try
        {
            // stack name needs to start with a letter
            // may only contain alphanumeric characters and "_-."
            String stackName = tc.getfTenantConsoleName() + "_" + vmc.getfName();
            
            // TODO: use openstack VM or Stack ID as ID?
            
            String initFile = fTmpDirectory + stackName + "_init.sh";
            String heatFile = fTmpDirectory + stackName + ".yaml";
            
            // build cloud init template from template
            genCloudInit(initFile, tc, vmc);
            
            // build heat template from template
            genHeatTemplate(heatFile, initFile, stackName, tc, vmc);
            
            // spawn using heat command line tool and template
            String command = fVmCreateScript + " " + fOSCredentials
                + " " + heatFile + " " + stackName;
            boolean error  = ProcessUtility.execute(command,
                new File(fTmpDirectory), fLogger);
            
            if(error)
            {
                throw new Exception("failed to create virtual machine using heat");
            }
            
            // cleanup
            //TODO: also clean up in case of an exception?
            new File(initFile).delete();
            new File(heatFile).delete();
            
            // retrieve IP and enter into configuration
            fLogger.log(Level.INFO, "Waiting for VM IP to become available");
            String ip = "";
            
            // takes a while to show up - more elegant solution needed
            for(int i = 0; i < fGetIpRetries; ++i)
            {
                Thread.sleep(fGetIpInt);
                
                ip = getVmIp(stackName);
                
                if(!ip.isEmpty())
                {
                    break;
                }
            }
            fLogger.log(Level.INFO, "got VM IP " + ip);
            vmc.getfIpAddresses().add(ip);
            
            
            // deploy certificates to new server
            // server needs to be booted properly for SSH to be running
            
            fLogger.log(Level.INFO, "Waiting for VM to become ready");
            
            command = "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -i "
                + fSshKeyFile + " " + fSshUser + "@" + ip + " " + "echo success";
            String response = "";
            for(int i = 0; i < fReadyCheckRetries; ++i)
            {
                Thread.sleep(fReadyCheckInt);
                
                response = ProcessUtility.getOuput(command, new File(fTmpDirectory));
                
                if(!response.isEmpty())
                {
                    break;
                }
            }
            
            fLogger.log(Level.INFO, "copying tenant certificates to " + ip);
            command = fDeployCertsScript + " " + tc.getfTenantConsoleName() + " " + ip;
            error = ProcessUtility.execute(command, new File(fScriptsDirectory), fLogger);
            // TODO: throws an error even if successful due to host key verification
        }
        catch(Exception e)
        {
            vm.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        vm.setState(EntityState.RUNNING);
    }
    
    private void genCloudInit(String initFile, TenantConfig tc, VMConfig vmc)
        throws Exception
    {
        Map<String, String> weaveConf = tc.getfServiceConfigs()
            .get(ServiceConstants.WEAVE_SERVICE);
        
        TemplateProcessor tmpProc = new TemplateProcessor(fVmInitTemplate);
        tmpProc.addValue(REV_PROXY_PROP, "false");
        // TODO: means to enable and disable Prometheus
        tmpProc.addValue(PROMETHEUS_PROP, "false");
        // common docker registry
        tmpProc.addValue(D_REGISTRY_PROP, fDockerRegistry);
        // tenant name (mesos marker)
        tmpProc.addValue(TENANT_NAME_PROP, tc.getfTenantConsoleName());
        // VM number (mesos marker) TODO: sequential number?
        tmpProc.addValue(VM_NAME_PROP, vmc.getfName());
        // zookeeper servers (masters)
        tmpProc.addValue(ZK_SERVERS_PROP, fClusterConfig.getfZookeeperHosts());
        // weave peers from existing VM(s)
        String peers = getWeavePeers(tc);
        weaveConf.put(ServiceConstants.WEAVE_PEERS, peers);
        tmpProc.addValue(WEAVE_PEERS_PROP, peers);
        // generated weave password for this tenant
        tmpProc.addValue(WEAVE_PASSWORD_PROP, weaveConf.get(ServiceConstants.WEAVE_PASSWORD));
        // weave host address
        tmpProc.addValue(WEAVE_HOST_ADDR_PROP, getNextHostIP(weaveConf, tc, vmc));
        // tenant weave network
        tmpProc.addValue(WEAVE_NET_PROP, weaveConf.get(ServiceConstants.WEAVE_NETWORK));
        // per-vm weave subnet
        tmpProc.addValue(WEAVE_VM_NET_PROP, getNextHostNetwork(weaveConf, tc, vmc));
        // consul datacenter name
        tmpProc.addValue(CONSUL_DTC_PROP, fConsulDatacenter);
        // consul peer(s)
        tmpProc.addValue(CONSUL_PEER_PROP, fConsulMaster);
        
        // TODO: generate based on the selected flavor?
        tmpProc.addValue(MESOS_CORES_PROP, vmc.getfVirtCPUs());
        tmpProc.addValue(MESOS_RAM_PROP, vmc.getfVirtRAM());
        tmpProc.addValue(MESOS_DISK_PROP, vmc.getfVirtDisk());
        
        tmpProc.writeTo(initFile);
    }
    
    private String getWeavePeers(TenantConfig tc)
    {
        String peers = "";
        
        for(VMConfig otherVm : tc.getfVirtualMachines())
        {
            if(otherVm.getfIpAddresses().size() > 0)
            {
                peers += otherVm.getfIpAddresses().get(0) + " ";
            }
        }
        
        return peers;
    }
    
    private String getNextHostIP(Map<String, String> weaveConf,
        TenantConfig tc, VMConfig vmc) throws Exception
    {
        // generate based on existing VMs
        String hostIpTemp = weaveConf.get(ServiceConstants.WEAVE_HOST_IP);
        
        // note IPs reserved for other VMs
        Set<String> ipsTaken = new HashSet<String>();
        for(VMConfig otherVm : tc.getfVirtualMachines())
        {
            ipsTaken.add(otherVm.getfWeaveHostIp());
        }

        // take first network that is not yet used
        String hostIp = null;
        for(int i = 1; i <= MAX_NET + 1; ++i)
        {
            if(i > MAX_NET)
            {
                throw new Exception("all weave host IPs in use");
            }

            hostIp = hostIpTemp.replace("y", Integer.toString(i));
            
            if(!ipsTaken.contains(hostIp))
            {
                break;
            }
        }
        
        // set in configuration
        vmc.setfWeaveHostIp(hostIp);
        
        return hostIp;
    }
    
    private String getNextHostNetwork(Map<String, String> weaveConf,
        TenantConfig tc, VMConfig vmc) throws Exception
    {
        // generate based on existing VMs
        String hostNetTemp = weaveConf.get(ServiceConstants.WEAVE_HOST_NETWORK);
        
        // note networks reserved for other VMs
        Set<String> netsTaken = new HashSet<String>();
        for(VMConfig otherVm : tc.getfVirtualMachines())
        {
            netsTaken.add(otherVm.getfWeaveHostNet());
        }
        
        // take first network that is not yet used
        String hostNet = null;
        for(int i = 1; i <= MAX_NET + 1; ++i)
        {
            if(i > MAX_NET)
            {
                throw new Exception("all weave subnets in use");
            }
            
            hostNet = hostNetTemp.replace("x", Integer.toString(i));
            
            if(!netsTaken.contains(hostNet))
            {
                break;
            }
        }

        // set in configuration
        vmc.setfWeaveHostNet(hostNet);
        
        return hostNet;
    }
    
    private void genHeatTemplate(String heatFile, String initFile, String stackName,
        TenantConfig tc, VMConfig vmc) throws Exception
    {
        TemplateProcessor tmpProc = new TemplateProcessor(fVmHeatTemplate);
        // VM name in OpenStack
        tmpProc.addValue(SERVER_NAME_PROP, stackName);
        // VM image to use
        tmpProc.addValue(IMAGE_NAME_PROP, vmc.getfImageName());
        // size of the VM
        tmpProc.addValue(FLAVOR_PROP, vmc.getfFlavorName());
        // availability zone
        tmpProc.addValue(AVAIL_ZONE_PROP, tc.getfAvailZone());
        // maintenance SSH key
        tmpProc.addValue(SSH_KEY_PROP, fSshKey);
        // cloud init script
        tmpProc.addValue(INIT_SCRIPT_PROP, initFile);
        // network ID
        tmpProc.addValue(NETWORK_ID_PROP, fVmNetwork);
        // subnet ID
        tmpProc.addValue(NETWORK_SUBNET_PROP, fVmSubnet);
        // security group
        tmpProc.addValue(SEC_GROUP_PROP, fSecGroup);
        
        tmpProc.writeTo(heatFile);
    }
    
    public void modifyVM(VirtualMachine vm) throws Exception
    {
        vm.setState(EntityState.MODIFYING);
        VMConfig vmc = vm.getConfig();
        
        TenantConfig tc = fWrapperService.getTenants()
            .get(vm.getTenantId()).getConfig();
        
        try
        {
            String stackName = tc.getfTenantConsoleName() + "_" + vmc.getfName();
            
        }
        catch(Exception e)
        {
            vm.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        vm.setState(EntityState.RUNNING);
    }
    
    public void deleteVM(VirtualMachine vm) throws Exception
    {
        vm.setState(EntityState.DELETING);
        VMConfig vmc = vm.getConfig();
        
        TenantConfig tc = fWrapperService.getTenants()
            .get(vm.getTenantId()).getConfig();
        
        try
        {
            String stackName = tc.getfTenantConsoleName() + "_" + vmc.getfName();

            // delete using heat command line tool
            String command = fVmDeleteScript + " " + fOSCredentials + " " + stackName;
            
            boolean error  = ProcessUtility.execute(command,
                new File(fTmpDirectory), fLogger);
            
            if(error)
            {
                throw new Exception("failed to delete virtual machine using heat");
            }
            
        }
        catch(Exception e)
        {
            vm.setState(EntityState.ERROR);
            
            //TODO: logging
            throw new Exception(e);
        }
        
        vm.setState(EntityState.DELETED);
    }
    
    /**
     * Deletes all virtual machines for a tenant.
     * 
     * @param tc
     * @throws Exception
     */
    public void deleteTenant(TenantConfig tc) throws Exception
    {
        String vmId = null;
        VirtualMachine vm = null;
        
        for(VMConfig vmc : tc.getfVirtualMachines())
        {
            vmId = vmc.getfId();
            
            // TODO: doesn't work, VM is null at this point
            vm = fWrapperService.getVMs().get(vmId);
            
            deleteVM(vm);
        }
    }
}
