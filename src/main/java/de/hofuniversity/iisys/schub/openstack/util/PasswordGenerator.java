package de.hofuniversity.iisys.schub.openstack.util;

import org.apache.commons.lang3.RandomStringUtils;

import de.hofuniversity.iisys.schub.openstack.api.WrapperService;

public class PasswordGenerator
{
    private final int fPasswordLength;
    
    public PasswordGenerator(WrapperService wrapperSvc)
    {
        this(wrapperSvc.getWrapperConfig().getfGenPasswordLength());
    }
    
    public PasswordGenerator(int passLen)
    {
        fPasswordLength = passLen;
    }
    
    public String genPassword()
    {
        String password = null;
        
        password = RandomStringUtils.randomAlphanumeric(fPasswordLength);
        
        return password;
    }
}
