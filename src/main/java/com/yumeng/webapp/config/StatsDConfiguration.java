package com.yumeng.webapp.config;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import com.yumeng.webapp.controller.HealthzController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatsDConfiguration {
    @Value("${publish.metrics}")
    private boolean publishMetrics;

    @Value("${metrics.server.hostname}")
    private String metricsServerHost;

    @Value("${metrics.server.port}")
    private int metricsServerPort;

    private static final Logger logger = LoggerFactory.getLogger(StatsDConfiguration.class);

    @Bean
    public StatsDClient statsDClient() {
        logger.info("Connect to statsD Client...");
        if(publishMetrics){
            logger.info("metrics is public");
            return new NonBlockingStatsDClient("csye6225", metricsServerHost, metricsServerPort);
        }
        logger.error("metrics is not public!");
        return new NoOpStatsDClient();

    }
}
