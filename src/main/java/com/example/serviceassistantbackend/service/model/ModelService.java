package com.example.serviceassistantbackend.service.model;

import com.example.serviceassistantbackend.dto.model.ModelRequestDTO;
import com.example.serviceassistantbackend.dto.model.ModelResponseDTO;

import java.util.List;

public interface ModelService {
    ModelResponseDTO createModel(ModelRequestDTO dto);
    ModelResponseDTO getModelById(Long id);
    List<ModelResponseDTO> getAllModels();
    ModelResponseDTO updateModel(Long id, ModelRequestDTO dto);
    void deleteModel(Long id);
}
