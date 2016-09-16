package de.hofuniversity.iisys.schub.openstack.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TemplateProcessor
{
    private final String fTemplate;
    
    private Map<String, String> fValues;
    
    public TemplateProcessor(String tempPath) throws Exception
    {
        fTemplate = readTemplate(tempPath);
        
        fValues = new LinkedHashMap<>();
    }
    
    private String readTemplate(String filePath) throws Exception
    {
        //read template from file
        final StringBuffer buffer = new StringBuffer();
        
        BufferedReader br = new BufferedReader(
            new FileReader(new File(filePath)));
        
        String line = br.readLine();
        while(line != null)
        {
            buffer.append(line);
            buffer.append("\n");
            
            line = br.readLine();
        }
        
        br.close();
        
        return buffer.toString();
    }
    
    public void addValue(String key, String value)
    {
        fValues.put(key, value);
    }
    
    public void clearValue(String key)
    {
        fValues.remove(key);
    }
    
    public void clear()
    {
        fValues.clear();
    }
    
    public void setValues(Map<String, String> values)
    {
        fValues = values;
    }
    
    public String process()
    {
        String result = fTemplate;
        
        for(Entry<String, String> valE : fValues.entrySet())
        {
            result = result.replace(valE.getKey(), valE.getValue());
        }
        
        return result;
    }
    
    public void writeTo(String filePath) throws Exception
    {
        String result = process();
        
        //write to disk
        BufferedWriter bw = new BufferedWriter(
            new FileWriter(new File(filePath)));
        
        bw.write(result);
        
        bw.flush();
        bw.close();
    }
}
