package com.example.serviceassistantbackend.service.model;

import com.example.serviceassistantbackend.dto.model.ModelRequestDTO;
import com.example.serviceassistantbackend.dto.model.ModelResponseDTO;
import com.example.serviceassistantbackend.entity.Model;
import com.example.serviceassistantbackend.repository.ModelRepository;
import com.example.serviceassistantbackend.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModelServiceImpl implements ModelService {
    private final ModelRepository modelRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public ModelResponseDTO createModel(ModelRequestDTO dto) {
        String trimmedName = dto.getName().trim();
        Optional<Model> existingModel = modelRepository.findByNameIgnoreCase(trimmedName);
        if (existingModel.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Model name already exists");
        }

        Model model = new Model();
        model.setName(trimmedName);
        Model savedModel = modelRepository.save(model);
        return mapToDto(savedModel);
    }

    @Override
    public ModelResponseDTO getModelById(Long id) {
        Model model = modelRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found"));
        return mapToDto(model);
    }

    @Override
    public List<ModelResponseDTO> getAllModels() {
        return modelRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public ModelResponseDTO updateModel(Long id, ModelRequestDTO dto) {
        Model model = modelRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found"));
        String trimmedName = dto.getName().trim();
        Optional<Model> existingModel = modelRepository.findByNameIgnoreCase(trimmedName);
        if (existingModel.isPresent() && !existingModel.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Model name already exists");
        }

        model.setName(trimmedName);
        Model updatedModel = modelRepository.save(model);
        return mapToDto(updatedModel);
    }

    @Override
    public void deleteModel(Long id) {
        Model model = modelRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found"));

        if (vehicleRepository.countByModel(model) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Cannot delete model assigned to vehicles");
        }

        modelRepository.deleteById(id);
    }

    private ModelResponseDTO mapToDto(Model model) {
        ModelResponseDTO dto = new ModelResponseDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setHasAssociatedVehicles(vehicleRepository.countByModel(model) > 0);
        return dto;
    }
}
