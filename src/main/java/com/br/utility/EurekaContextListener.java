package com.br.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class EurekaContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize the Eureka Client when the application starts
        EurekaClientInitializer.initEurekaClient(sce.getServletContext().getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Shutdown the Eureka Client when the application stops
        EurekaClientInitializer.shutdownEurekaClient();
    }
}
