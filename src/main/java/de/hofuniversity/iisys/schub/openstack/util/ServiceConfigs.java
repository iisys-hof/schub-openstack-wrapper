package de.hofuniversity.iisys.schub.openstack.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ServiceConfigs
{
    private static final String PROPS = "schub-services";
    
    private static final ServiceConfigs INSTANCE = new ServiceConfigs();
    
    public static ServiceConfigs getInstance()
    {
        return INSTANCE;
    }
    
    private final Map<String, String> fProperties;
    
    private ServiceConfigs()
    {
        fProperties = new HashMap<String, String>();
        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ResourceBundle rb = ResourceBundle.getBundle(PROPS,
                Locale.getDefault(), loader);
        
        String key = null;
        String value = null;
        final Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements())
        {
            key = keys.nextElement();
            value = rb.getString(key);

            fProperties.put(key, value);
        }
    }
    
    public String getString(String key)
    {
        return fProperties.get(key);
    }
}
