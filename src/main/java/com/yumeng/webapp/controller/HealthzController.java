package com.yumeng.webapp.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HealthzController {
    @GetMapping("/healthz")
    public ResponseEntity getHealthz() {
        return ResponseEntity.ok().build();
    }
}
