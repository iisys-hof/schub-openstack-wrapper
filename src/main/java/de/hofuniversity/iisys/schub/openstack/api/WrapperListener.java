package de.hofuniversity.iisys.schub.openstack.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WrapperListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        //initialize wrapper backend
        WrapperService.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        //clean shutdown
        WrapperService.getInstance().shutdown();
    }
}
