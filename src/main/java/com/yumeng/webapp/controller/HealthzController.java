package com.yumeng.webapp.controller;

import com.yumeng.webapp.config.AWSConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HealthzController {
    @Autowired
    private StatsDClient statsDClient;
    private static final Logger logger = LoggerFactory.getLogger(HealthzController.class);
    @GetMapping("/healthz")
    public ResponseEntity getHealthz() {
        statsDClient.incrementCounter("healthz.get");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[GET]test healthz successful");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        statsDClient.incrementCounter("health.get");
        statsDClient.incrementCounter("all.api.call");
        logger.info("[GET]test health successful");
        return ResponseEntity.ok().build();
    }
}
