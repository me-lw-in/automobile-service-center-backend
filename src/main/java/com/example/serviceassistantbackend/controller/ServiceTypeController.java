package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.service.ServiceTypeDTO;
import com.example.serviceassistantbackend.service.servicetype.ServiceTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    @GetMapping
    public ResponseEntity<List<ServiceTypeDTO>> getAllServiceTypes() {
        return ResponseEntity.ok(serviceTypeService.getAllServiceTypes());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ServiceTypeDTO> createServiceType(@RequestBody ServiceTypeDTO dto) {
        return ResponseEntity.ok(serviceTypeService.createServiceType(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@PathVariable Long id, @RequestBody ServiceTypeDTO dto) {
        return ResponseEntity.ok(serviceTypeService.updateServiceType(id, dto));
    }
}
