package de.hofuniversity.iisys.schub.openstack.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hofuniversity.iisys.schub.openstack.config.TenantConfig;
import de.hofuniversity.iisys.schub.openstack.config.VMConfig;

public class ValueValidator
{
    private static final int MIN_PW_LENGTH = 8;
    
    private final Set<String> fKnownServices;
    
    private final Set<Character> fUmlauts;
    
    private final Set<Character> fAllowedSChars;
    
    public ValueValidator()
    {
        fKnownServices = new HashSet<String>();
        
        for(String service : ServiceConstants.SERVICES)
        {
            fKnownServices.add(service);
        }
        
        // TODO: does this work everywhere?
        fUmlauts = new HashSet<Character>();
        fUmlauts.add('ä');
        fUmlauts.add('Ä');
        fUmlauts.add('ö');
        fUmlauts.add('Ö');
        fUmlauts.add('ü');
        fUmlauts.add('Ü');
        
        fAllowedSChars = new HashSet<Character>();
        fAllowedSChars.add(' ');
        fAllowedSChars.add('-');
        fAllowedSChars.add('_');
        fAllowedSChars.add('.');
        fAllowedSChars.addAll(fUmlauts);
    }
    
    private void checkSpecialCharacters(String value) throws Exception
    {
        for(char c : value.toCharArray())
        {
            if(!(
                '0'<=c && c<='9'
                || 'a' <= c && c <= 'z'
                || 'A' <= c && c <= 'Z'
                || fAllowedSChars.contains(c)
              ))
            {
                throw new Exception("illegal character '" + c + "'");
            }
        }
    }
    
    private void checkBlanks(String value) throws Exception
    {
        if(value.contains(" ")
            || value.contains("\t")
            || value.contains("\n"))
        {
            throw new Exception("must not contain blanks");
        }
    }
    
    private void checkUmlauts(String value) throws Exception
    {
        for(char c : value.toCharArray())
        {
            if(fUmlauts.contains(c))
            {
                throw new Exception("unexpected umlaut character '" + c + "'");
            }
        }
    }
    
    private void checkInteger(String value) throws Exception
    {
        int i = Integer.parseInt(value);
        if(i < 0)
        {
            throw new Exception("negative values not expected");
        }
    }
    
    private void checkDouble(String value) throws Exception
    {
        double d = Double.parseDouble(value);
        if(d < 0)
        {
            throw new Exception("negative values not expected");
        }
    }
    
    public void validateTenantConfig(TenantConfig tc) throws Exception
    {
        // check for illegal characters
        String name = tc.getfTenantName();
        checkSpecialCharacters(name);
        
        String conName = tc.getfTenantConsoleName();
        checkSpecialCharacters(conName);
        checkUmlauts(conName);
        checkBlanks(conName);
        
        String desc = tc.getfTenantDescription();
        checkSpecialCharacters(desc);
    }
    
    public void validateVMConfig(VMConfig vmc) throws Exception
    {
        // check for illegal characters
        String name = vmc.getfName();
        checkSpecialCharacters(name);
        checkUmlauts(name);
        checkBlanks(name);
        
        String flavor = vmc.getfFlavorName();
        checkSpecialCharacters(flavor);
        checkBlanks(flavor);
        
        String image = vmc.getfImageName();
        checkSpecialCharacters(image);
        checkBlanks(image);
        
        String cpus = vmc.getfVirtCPUs();
        checkInteger(cpus);
        
        String ram = vmc.getfVirtRAM();
        checkInteger(ram);
        
        String disk = vmc.getfVirtDisk();
        checkInteger(disk);
        
        // check for unknown services
        List<String> services = vmc.getfServices();
        for(String service : services)
        {
            if(!fKnownServices.contains(service))
            {
                throw new Exception("unknown service '" + service + "'");
            }
        }
    }
    
    public void validatePassword(String password) throws Exception
    {
        if(password == null || password.isEmpty())
        {
            throw new Exception("Password empty");
        }
        
        if(password.length() < MIN_PW_LENGTH)
        {
            throw new Exception("Password too short");
        }
        
        checkSpecialCharacters(password);
        checkBlanks(password);
    }
}
