package de.hofuniversity.iisys.schub.openstack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil
{
    public static String get(String url) throws Exception
    {
        StringBuffer buff = new StringBuffer();
        
        URL urlObj = new URL(url);
        final HttpURLConnection connection =
            (HttpURLConnection) urlObj.openConnection();
        
        connection.setRequestMethod("GET");
        
        //read reply
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
        
        String line = reader.readLine();
        while(line != null)
        {
            buff.append(line + "\n");
            line = reader.readLine();
        }
        
        reader.close();
        
        return buff.toString();
    }
    
    public static String sendJsonFile(String file, String url) throws Exception
    {
        // read json file
        String json = "";
        File jsonFile = new File(file);
        
        BufferedReader br = new BufferedReader(new FileReader(jsonFile));
        String line = br.readLine();
        while(line != null)
        {
            json += line;
            line = br.readLine();
        }
        
        //set parameters for sending
        URL urlObj = new URL(url);
        final HttpURLConnection connection =
            (HttpURLConnection) urlObj.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(
            json.length()));
        
        //send JSON activity
        OutputStreamWriter writer = new OutputStreamWriter(
            connection.getOutputStream(), "UTF-8");
        writer.write(json);
        writer.flush();
        
        //read reply
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
        
        String response = "";
        line = reader.readLine();
        while(line != null)
        {
            response += line + "\n";
            line = reader.readLine();
        }
        
        reader.close();
        
        return response;
    }
    
    public static String sendDelete(String url) throws Exception
    {
        //set parameters for sending
        URL urlObj = new URL(url);
        final HttpURLConnection connection =
            (HttpURLConnection) urlObj.openConnection();
        
        connection.setRequestMethod("DELETE");
        
        //read reply
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
        
        String response = "";
        String line = reader.readLine();
        while(line != null)
        {
            response += line + "\n";
            line = reader.readLine();
        }
        
        reader.close();
        
        return response;
    }
}
