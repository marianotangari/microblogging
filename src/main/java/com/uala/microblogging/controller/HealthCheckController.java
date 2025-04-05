package com.uala.microblogging.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/health")
@AllArgsConstructor
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("Application is up and running");
    }
}