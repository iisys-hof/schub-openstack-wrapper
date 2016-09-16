package de.hofuniversity.iisys.schub.openstack.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.WrapperConfig;

public class ConfigStorage
{
    private final WrapperConfig fConfig;
    private final String fDirPath;
    
    public ConfigStorage(WrapperConfig config)
    {
        fConfig = config;
        
        fDirPath = fConfig.getfTenantsDirectory();
    }
    
    private File getTenantFile(TenantConfig tenant)
    {
        return new File(fDirPath + tenant.getfTenantConsoleName() + ".json");
    }
    
    public void storeTenants(List<TenantConfig> tenants) throws Exception
    {
        //clear tenants directory?
        //TODO: safely move a backup away first
        File directory = new File(fDirPath);
        for(File file : directory.listFiles())
        {
            file.delete();
        }
        
        File tenFile = null;
        
        for(TenantConfig tenant : tenants)
        {
            tenFile = getTenantFile(tenant);
            storeTenant(tenFile, tenant);
        }
    }
    
    public void storeTenant(TenantConfig tenant) throws Exception
    {
        File tenFile = getTenantFile(tenant);
        storeTenant(tenFile, tenant);
    }
    
    private void storeTenant(File file, TenantConfig tenant) throws Exception
    {
        //convert
        String json = TenantConfigConverter.toJSON(tenant).toString();

        //write
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(json);
        bw.flush();
        bw.close();
    }
    
    public void removeTenant(TenantConfig tenant) throws Exception
    {
        File tenFile = getTenantFile(tenant);
        if(tenFile.exists())
        {
            tenFile.delete();
        }
    }
}
