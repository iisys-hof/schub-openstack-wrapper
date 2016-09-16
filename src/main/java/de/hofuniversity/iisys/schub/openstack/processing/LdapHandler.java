package de.hofuniversity.iisys.schub.openstack.processing;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.util.ConfigLoader;
import de.hofuniversity.iisys.schub.openstack.util.PasswordGenerator;
import de.hofuniversity.iisys.schub.openstack.util.ProcessUtility;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

/**
 * Handles creation and modification of tenants' LDAP directories
 */
public class LdapHandler
{
    private static final String SCRIPTS_PROPS = "schub-scripts";
    
    private static final String LDAP_GEN_SCRIPT_PROP = "schub.script.ldap.create";
    private static final String LDAP_DEL_SCRIPT_PROP = "schub.script.ldap.delete";
    
    private final File fScriptsPath;
    
    private final String fLdapUri;
    
    private final String fCreateScript;
    private final String fDeleteScript;
    
    private final PasswordGenerator fPwGen;
    
    private final Logger fLogger;
    
    public LdapHandler(WrapperService wrapperSvc)
    {
        fScriptsPath = new File(wrapperSvc.getWrapperConfig().getfScriptsDirectory());
        
        fLdapUri = "ldap://" + wrapperSvc.getClusterConfig().getfLdapServer();
        
        ConfigLoader cl = wrapperSvc.getConfigLoader();
        Map<String, String> scriptsConf = cl.getProperties(SCRIPTS_PROPS);
        
        fCreateScript = scriptsConf.get(LDAP_GEN_SCRIPT_PROP);
        fDeleteScript = scriptsConf.get(LDAP_DEL_SCRIPT_PROP);
        
        fPwGen = new PasswordGenerator(wrapperSvc);
        
        fLogger = Logger.getLogger(this.getClass().getName());
    }
    
    public void initTenant(TenantConfig config) throws Exception
    {
        String tenantName = config.getfTenantConsoleName();
        
        // sub-admin's credentials and general configuration
        // TODO: generate based on templates
        String baseDn = "dc=" + tenantName + ",dc=org";
        String userDn = "ou=people";
        String adminDn = "cn=admin," + baseDn;
        String adminPass = fPwGen.genPassword();
        
        // create LDAP tree and sub-admin
        // execute creation script with parameters
        try
        {
            String command = "./" + fCreateScript + " "
                + tenantName + " " + adminPass;
            
            fLogger.log(Level.INFO, "executing: '" + fCreateScript + "' in " + fScriptsPath);
            
            boolean error = ProcessUtility.execute(command, fScriptsPath, fLogger);
            
            if(!error)
            {
                fLogger.log(Level.INFO, "finished creating ldap tenant");
            }
            else
            {
                // TODO: also logs to stderr if there is no error
//                throw new Exception("failed to create ldap tenant");
            }
        }
        catch(Exception e)
        {
            fLogger.log(Level.SEVERE, "failed to create ldap tenant", e);
            throw e;
        }
        
        // store data in the tenant configuration
        Map<String, Map<String, String>> svcConfs = config.getfServiceConfigs();

        // Camunda
        Map<String, String> camundaConf = svcConfs.get(ServiceConstants.CAMUNDA_SERVICE);
        camundaConf.put(ServiceConstants.CAMUNDA_LDAP_SERVER, fLdapUri);
        camundaConf.put(ServiceConstants.CAMUNDA_LDAP_BASE_DN, baseDn);
        camundaConf.put(ServiceConstants.CAMUNDA_LDAP_USER, adminDn);
        camundaConf.put(ServiceConstants.CAMUNDA_LDAP_PASSWORD, adminPass);
        camundaConf.put(ServiceConstants.CAMUNDA_LDAP_USER_DN, userDn + "," + baseDn);
        
        // CAS
        Map<String, String> casConf = svcConfs.get(ServiceConstants.CAS_SERVICE);
        casConf.put(ServiceConstants.CAS_LDAP_SERVER, fLdapUri);
        casConf.put(ServiceConstants.CAS_LDAP_USER, adminDn);
        casConf.put(ServiceConstants.CAS_LDAP_PASSWORD, adminPass);
        casConf.put(ServiceConstants.CAS_LDAP_USER_DN, userDn + "," + baseDn);
        
        // Liferay
        Map<String, String> liferayConf = svcConfs.get(ServiceConstants.LIFERAY_SERVICE);
        liferayConf.put(ServiceConstants.LIFERAY_LDAP_SERVER, fLdapUri);
        liferayConf.put(ServiceConstants.LIFERAY_LDAP_BASE_DN, baseDn);
        liferayConf.put(ServiceConstants.LIFERAY_LDAP_USER, adminDn);
        liferayConf.put(ServiceConstants.LIFERAY_LDAP_PASSWORD, adminPass);
        liferayConf.put(ServiceConstants.LIFERAY_LDAP_USER_DN, userDn + "," + baseDn);
        
        // Nuxeo
        Map<String, String> nuxeoConf = svcConfs.get(ServiceConstants.NUXEO_SERVICE);
        nuxeoConf.put(ServiceConstants.NUXEO_LDAP_USER, adminDn);
        nuxeoConf.put(ServiceConstants.NUXEO_LDAP_PASSWORD, adminPass);
        nuxeoConf.put(ServiceConstants.NUXEO_LDAP_SERVER, fLdapUri);
        nuxeoConf.put(ServiceConstants.NUXEO_LDAP_USER_BASE, userDn + "," + baseDn);
        
        // Open-Xchange
        Map<String, String> oxConf = svcConfs.get(ServiceConstants.OPEN_XCHANGE_SERVICE);
        oxConf.put(ServiceConstants.OX_LDAP_SERVER, fLdapUri);
        oxConf.put(ServiceConstants.OX_LDAP_USER, adminDn);
        oxConf.put(ServiceConstants.OX_LDAP_PASSWORD, adminPass);
        oxConf.put(ServiceConstants.OX_LDAP_USER_BASE, userDn + "," + baseDn);
    }
    
    public void deleteTenant(TenantConfig config) throws Exception
    {
        String tenantName = config.getfTenantConsoleName();
        
        // delete LDAP tree and sub-admin
        // execute deletion script with parameters
        try
        {
            String command = "./" + fDeleteScript + " "
                + tenantName;
            
            fLogger.log(Level.INFO, "executing: '" + fDeleteScript + "' in " + fScriptsPath);
            
            ProcessUtility.execute(command, fScriptsPath, fLogger);
            
            fLogger.log(Level.INFO, "finished deleting ldap tenant");
        }
        catch(Exception e)
        {
            fLogger.log(Level.SEVERE, "failed to delete ldap tenant", e);
            throw e;
        }
    }
}
