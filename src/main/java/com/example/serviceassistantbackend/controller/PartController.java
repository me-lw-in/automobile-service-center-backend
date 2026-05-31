package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.part.PartDTO;
import com.example.serviceassistantbackend.service.part.PartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<List<PartDTO>> getAllParts() {
        return ResponseEntity.ok(partService.getAllParts());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<PartDTO> createPart(@RequestBody @Valid PartDTO dto) {
        return ResponseEntity.ok(partService.createPart(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<PartDTO> updatePart(@PathVariable Long id, @RequestBody @Valid PartDTO dto) {
        return ResponseEntity.ok(partService.updatePart(id, dto));
    }
}
