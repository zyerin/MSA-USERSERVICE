package com.example.user_service.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private boolean isReady = true; // 기본 상태는 UP으로 설정

    @Override
    public Health health() {
        if (isReady) {
            return Health.up().withDetail("Readiness Check", "Service is ready").build();
        } else {
            return Health.down().withDetail("Readiness Check", "Service is not ready").build();
        }
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }
}

