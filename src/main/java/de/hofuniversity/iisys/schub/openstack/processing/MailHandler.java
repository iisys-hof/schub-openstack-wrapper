package de.hofuniversity.iisys.schub.openstack.processing;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;
import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.util.ConfigLoader;
import de.hofuniversity.iisys.schub.openstack.util.ProcessUtility;
import de.hofuniversity.iisys.schub.openstack.util.ServiceConstants;

/**
 * Handles the creation of appropriate eMail accounts in postfix and dovecot.
 */
public class MailHandler
{
    private static final String SCRIPTS_PROPS = "schub-scripts";
    
    private static final String MAIL_GEN_SCRIPT_PROP = "schub.script.mail.create";
    private static final String MAIL_DEL_SCRIPT_PROP = "schub.script.mail.delete";

    private final File fScriptsPath;
    
    private final String fCreateScript;
    private final String fDeleteScript;
    
    private final String fImapServer;
    private final String fSmtpServer;
    
    private final Logger fLogger;
    
    public MailHandler(WrapperService wrapperSvc)
    {
        fScriptsPath = new File(wrapperSvc.getWrapperConfig().getfScriptsDirectory());

        ConfigLoader cl = wrapperSvc.getConfigLoader();
        Map<String, String> scriptsConf = cl.getProperties(SCRIPTS_PROPS);
        
        fCreateScript = scriptsConf.get(MAIL_GEN_SCRIPT_PROP);
        fDeleteScript = scriptsConf.get(MAIL_DEL_SCRIPT_PROP);
        
        fImapServer = wrapperSvc.getClusterConfig().getfImapServer();
        fSmtpServer = wrapperSvc.getClusterConfig().getfSmtpServer();
        
        fLogger = Logger.getLogger(this.getClass().getName());
    }
    
    public void initTenant(TenantConfig config) throws Exception
    {
        String tenantName = config.getfTenantConsoleName();
        String mailDomain = config.getfTenantMailDomain();
        
        // execute creation script with parameters
        try
        {
            String command = "./" + fCreateScript + " "
                + tenantName + " " + mailDomain;
            
            fLogger.log(Level.INFO, "executing: '" + fCreateScript + "' in " + fScriptsPath);
            
            boolean error = ProcessUtility.execute(command, fScriptsPath, fLogger);
            
            if(!error)
            {
                fLogger.log(Level.INFO, "finished creating mail tenant");
            }
            else
            {
                // TODO: also logs to stderr if there is no error
//              throw new Exception("failed to create ldap tenant");
            }
        }
        catch(Exception e)
        {
            fLogger.log(Level.SEVERE, "failed to create mail tenant", e);
            throw e;
        }
    }
    
    public void deleteTenant(TenantConfig config) throws Exception
    {
        String tenantName = config.getfTenantConsoleName();
        
        // execute deletion script with parameters
        try
        {
            String command = "./" + fDeleteScript + " "
                + tenantName;
            
            fLogger.log(Level.INFO, "executing: '" + fDeleteScript + "' in " + fScriptsPath);
            
            ProcessUtility.execute(command, fScriptsPath, fLogger);
            
            fLogger.log(Level.INFO, "finished deleting mail tenant");
        }
        catch(Exception e)
        {
            fLogger.log(Level.SEVERE, "failed to delete mail tenant", e);
            throw e;
        }
    }
}
