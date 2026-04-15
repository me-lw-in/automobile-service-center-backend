package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.service.ServiceDTO;
import com.example.serviceassistantbackend.service.jobservices.JobServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/services")
class ServiceController {

    private final JobServices jobServices;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        var body = jobServices.getAllServices();
        return ResponseEntity.ok(body);
    }
}
