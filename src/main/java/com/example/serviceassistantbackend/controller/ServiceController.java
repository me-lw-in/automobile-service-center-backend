package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.service.ServiceDTO;
import com.example.serviceassistantbackend.service.jobservices.JobServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO dto) {
        return ResponseEntity.ok(jobServices.createService(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Long id, @RequestBody ServiceDTO dto) {
        return ResponseEntity.ok(jobServices.updateService(id, dto));
    }
}
