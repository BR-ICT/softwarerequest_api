package com.br.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;

public class EurekaClientInitializer {

    private static final Logger logger = LoggerFactory.getLogger(EurekaClientInitializer.class);

    private static EurekaClient eurekaClient;
    private static ApplicationInfoManager applicationInfoManager;

    /**
     * @param contextPath the webapp's context path from ServletContext (e.g. "/itemrequest_api"),
     *                    so the status link always matches the deployed WAR name instead of a
     *                    hardcoded path copied from another project.
     */
    public static void initEurekaClient(final String contextPath) {
        logger.info("Initializing Eureka Client (context path: {})...", contextPath);
        try {
            EurekaInstanceConfig instanceConfig = new MyDataCenterInstanceConfig() {
                @Override
                public String getInstanceId() {
                    return getHostName(false) + ":" + getAppname() + ":" + getNonSecurePort();
                }
                @Override
                public String getStatusPageUrl() {
                    return "http://" + getIpAddress() + ":" + getNonSecurePort() + contextPath + "/status";
                }
            };
            applicationInfoManager = new ApplicationInfoManager(
                instanceConfig,
                new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get()
            );

            eurekaClient = new DiscoveryClient(applicationInfoManager, new DefaultEurekaClientConfig());
            applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);

            logger.info("Eureka Client initialized. App: {}, Status: UP", instanceConfig.getAppname());
        } catch (Exception e) {
            logger.error("Failed to initialize Eureka Client: {}", e.getMessage(), e);
        }
    }

    public static void shutdownEurekaClient() {
        logger.info("Shutting down Eureka Client...");
        if (eurekaClient != null) {
            eurekaClient.shutdown();
            eurekaClient = null;
        }
    }
}
