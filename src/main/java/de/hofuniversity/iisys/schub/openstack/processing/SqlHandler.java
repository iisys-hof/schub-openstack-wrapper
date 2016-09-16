package de.hofuniversity.iisys.schub.openstack.processing;

import java.io.File;
import java.io.IOException;
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
 * Handles the creation of databases and database users for a tenant in the
 * shared database cluster.
 */
public class SqlHandler
{
    private static final String SCRIPTS_PROPS = "schub-scripts";
    
    private static final String SQL_GEN_SCRIPT_PROP = "schub.script.databases.create";
    private static final String SQL_DROP_SCRIPT_PROP = "schub.script.databases.drop";
    
    private final File fScriptsPath;
    
    private final String fDatabaseHost;
    private final String fDatabasePort;
    
    private final String fDatabaseLegacyHost;
    private final String fDatabaseLegacyPort;
    
    private final String fCreateScript;
    private final String fDropScript;
    
    private final PasswordGenerator fPwGen;
    
    private final Logger fLogger;
    
    public SqlHandler(WrapperService wrapperSvc)
    {
        fScriptsPath = new File(wrapperSvc.getWrapperConfig().getfScriptsDirectory());
        
        //TODO: support for multiple hosts?
        fDatabaseHost = wrapperSvc.getClusterConfig().getfSqlHosts()[0];
        fDatabasePort = wrapperSvc.getClusterConfig().getfSqlPort();
        
        fDatabaseLegacyHost = wrapperSvc.getClusterConfig().getfSqlLegacyHosts()[0];
        fDatabaseLegacyPort = wrapperSvc.getClusterConfig().getfSqlLegacyPort();
        
        ConfigLoader cl = wrapperSvc.getConfigLoader();
        Map<String, String> scriptsConf = cl.getProperties(SCRIPTS_PROPS);
        
        fCreateScript = scriptsConf.get(SQL_GEN_SCRIPT_PROP);
        fDropScript = scriptsConf.get(SQL_DROP_SCRIPT_PROP);
        
        fPwGen = new PasswordGenerator(wrapperSvc);
        
        fLogger = Logger.getLogger(this.getClass().getName());
    }
    
    public void initTenant(TenantConfig config) throws Exception
    {
        String tenantName = config.getfTenantConsoleName();
        
        // replace all dashes since they're not supported in names
        tenantName = tenantName.replace("-", "_");
        
        //TODO: template-based database user/name generation
        
        // Camunda DB
        String camundaDatabase = "camunda_" + tenantName;
        String camundaUser = "camunda_" + tenantName;
        String camundaPass = fPwGen.genPassword();
        
        // Liferay DB
        String liferayDatabase = "liferay_" + tenantName;
        String liferayUser = "liferay_" + tenantName;
        String liferayPass = fPwGen.genPassword();
        
        // Nuxeo DB
        String nuxeoDatabase = "nuxeo_" + tenantName;
        String nuxeoUser = "nuxeo_" + tenantName;
        String nuxeoPass = fPwGen.genPassword();
        
        // Open-Xchange DB
        String openXchangeConfDb = "open_xchange_" + tenantName;
        String openXchangeDataDb = "open_xchange_data_" + tenantName;
        String openXchangeUser = "open_xchange_" + tenantName;
        String openXchangePass = fPwGen.genPassword();
        
        // execute creation script with parameters
        try
        {
            String command = "./" + fCreateScript + " "
                + tenantName + " "
                + camundaPass + " "
                + liferayPass + " "
                + nuxeoPass + " "
                + openXchangePass;
            
            fLogger.log(Level.INFO, "executing: '" + fCreateScript + "' in " + fScriptsPath);

            boolean error = ProcessUtility.execute(command, fScriptsPath, fLogger);
            
            if(!error)
            {
                fLogger.log(Level.INFO, "finished creating databases");
            }
            else
            {
                throw new Exception("failed to create databases");
            }
        }
        catch (IOException e)
        {
            fLogger.log(Level.SEVERE, "failed to create databases", e);
            throw e;
        }
        
        // TODO: store data in the tenant configuration
        Map<String, Map<String, String>> svcConfs = config.getfServiceConfigs();
        
        // Camunda
        // does not seem to always work properly using MaxScale -> use direct legacy connection when in doubt
        Map<String, String> camundaConf = svcConfs.get(ServiceConstants.CAMUNDA_SERVICE);
        camundaConf.put(ServiceConstants.CAMUNDA_DB_HOST, fDatabaseHost);
        camundaConf.put(ServiceConstants.CAMUNDA_DB_PORT, fDatabasePort);
        camundaConf.put(ServiceConstants.CAMUNDA_DB_NAME, camundaDatabase);
        camundaConf.put(ServiceConstants.CAMUNDA_DB_USER, camundaUser);
        camundaConf.put(ServiceConstants.CAMUNDA_DB_PASSWORD, camundaPass);
        
        // Liferay
        Map<String, String> liferayConf = svcConfs.get(ServiceConstants.LIFERAY_SERVICE);
        liferayConf.put(ServiceConstants.LIFERAY_DB_HOST, fDatabaseHost);
        liferayConf.put(ServiceConstants.LIFERAY_DB_PORT, fDatabasePort);
        liferayConf.put(ServiceConstants.LIFERAY_DB_NAME, liferayDatabase);
        liferayConf.put(ServiceConstants.LIFERAY_DB_USER, liferayUser);
        liferayConf.put(ServiceConstants.LIFERAY_DB_PASSWORD, liferayPass);
        
        // Nuxeo
        Map<String, String> nuxeoConf = svcConfs.get(ServiceConstants.NUXEO_SERVICE);
        nuxeoConf.put(ServiceConstants.NUXEO_DB_HOST, fDatabaseHost);
        nuxeoConf.put(ServiceConstants.NUXEO_DB_PORT, fDatabasePort);
        nuxeoConf.put(ServiceConstants.NUXEO_DB_NAME, nuxeoDatabase);
        nuxeoConf.put(ServiceConstants.NUXEO_DB_USER, nuxeoUser);
        nuxeoConf.put(ServiceConstants.NUXEO_DB_PASSWORD, nuxeoPass);
        
        // Open-Xchange
        Map<String, String> oxConf = svcConfs.get(ServiceConstants.OPEN_XCHANGE_SERVICE);
        oxConf.put(ServiceConstants.OX_DB_HOST, fDatabaseHost);
        oxConf.put(ServiceConstants.OX_DB_PORT, fDatabasePort);
        oxConf.put(ServiceConstants.OX_CONF_DB_NAME, openXchangeConfDb);
        oxConf.put(ServiceConstants.OX_DATA_DB_NAME, openXchangeDataDb);
        oxConf.put(ServiceConstants.OX_DB_USER, openXchangeUser);
        oxConf.put(ServiceConstants.OX_DB_PASSWORD, openXchangePass);
    }
    
    public void deleteTenant(TenantConfig config) throws Exception
    {
        String tenantName = config.getfTenantConsoleName();
        
        // replace all dashes since they're not supported in names
        tenantName = tenantName.replace("-", "_");
        
        //TODO: actually delete databases and account, lock them or store elsewhere?
        
        //TODO: execute creation script with parameters
        try
        {
            String command = "./" + fDropScript + " " + tenantName;
            
            fLogger.log(Level.INFO, "executing: '" + fDropScript + "' in " + fScriptsPath);
            
            boolean error = ProcessUtility.execute(command, fScriptsPath, fLogger);
            
            fLogger.log(Level.INFO, "finished deleting databases");
            if(error)
            {
                //TODO: ?
            }
        }
        catch (IOException e)
        {
            fLogger.log(Level.SEVERE, "failed to create databases", e);
            throw e;
        }
    }
}
