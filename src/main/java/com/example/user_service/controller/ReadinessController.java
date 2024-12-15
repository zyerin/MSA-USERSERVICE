package com.example.user_service.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
public class ReadinessController {

    private final CustomHealthIndicator readinessHealthIndicator;

    public ReadinessController(CustomHealthIndicator readinessHealthIndicator) {
        this.readinessHealthIndicator = readinessHealthIndicator;
    }

    @PatchMapping("/down")
    public void markAsDown() {
        readinessHealthIndicator.setReady(false);
    }

    @PatchMapping("/up")
    public void markAsUp() {
        readinessHealthIndicator.setReady(true);
    }
}

