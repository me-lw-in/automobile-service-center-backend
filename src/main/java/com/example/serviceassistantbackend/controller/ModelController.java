package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.model.ModelRequestDTO;
import com.example.serviceassistantbackend.dto.model.ModelResponseDTO;
import com.example.serviceassistantbackend.service.model.ModelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@AllArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ModelResponseDTO createModel(@RequestBody @Valid ModelRequestDTO dto) {
        return modelService.createModel(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ModelResponseDTO getModel(@PathVariable Long id) {
        return modelService.getModelById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<ModelResponseDTO> getAllModels() {
        return modelService.getAllModels();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ModelResponseDTO updateModel(@PathVariable Long id, @RequestBody @Valid ModelRequestDTO dto) {
        return modelService.updateModel(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
    }
}
