package com.yumeng.webapp.controller;

import com.yumeng.webapp.config.AWSConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HealthzController {
    private static final Logger logger = LoggerFactory.getLogger(HealthzController.class);
    @GetMapping("/healthz")
    public ResponseEntity getHealthz() {
        logger.info("[GET]test healthz successful");
        return ResponseEntity.ok().build();
    }
}
