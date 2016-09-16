package de.hofuniversity.iisys.schub.openstack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessUtility
{
    public static boolean execute(String command, File folder, Logger logger) throws Exception
    {
        //TODO: output full command in debug mode
        
        Process proc = Runtime.getRuntime().exec(command, new String[]{}, folder);
        
        // read output and log
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        
        String line = reader.readLine();
        while(line != null)
        {
            logger.log(Level.INFO, line);
            line = reader.readLine();
        }
        
        boolean error = false;
        reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        line = reader.readLine();
        while(line != null)
        {
            //TODO: is this a valid sign of an error?
            error = true;
            logger.log(Level.SEVERE, line);
            line = reader.readLine();
        }
        
        return error;
    }
    
    public static String getOuput(String command, File folder) throws Exception
    {
        //TODO: output full command in debug mode
        
        StringBuffer output = new StringBuffer();
        
        Process proc = Runtime.getRuntime().exec(command, new String[]{}, folder);
        
        // read output and store
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        
        String line = reader.readLine();
        while(line != null)
        {
            output.append(line + "\n");
            line = reader.readLine();
        }
        
        return output.toString();
    }
}
