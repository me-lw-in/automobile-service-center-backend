package com.example.serviceassistantbackend.service.servicetype;

import com.example.serviceassistantbackend.dto.service.ServiceTypeDTO;
import com.example.serviceassistantbackend.entity.ServiceType;
import com.example.serviceassistantbackend.repository.ServiceTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public List<ServiceTypeDTO> getAllServiceTypes() {
        var list = serviceTypeRepository.findAll();
        List<ServiceTypeDTO> dtos = new ArrayList<>();
        for (ServiceType type : list) {
            var dto = new ServiceTypeDTO();
            dto.setId(type.getId());
            dto.setName(type.getName());
            dto.setDescription(type.getDescription());
            dto.setPrice(type.getPrice());
            dtos.add(dto);
        }
        return dtos;
    }

    public ServiceTypeDTO createServiceType(ServiceTypeDTO dto) {
        var type = new ServiceType();
        type.setName(dto.getName().trim());
        type.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        type.setPrice(dto.getPrice());
        type = serviceTypeRepository.save(type);

        dto.setId(type.getId());
        return dto;
    }

    public ServiceTypeDTO updateServiceType(Long id, ServiceTypeDTO dto) {
        var type = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ServiceType not found with id: " + id));
        type.setName(dto.getName().trim());
        type.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        type.setPrice(dto.getPrice());
        type = serviceTypeRepository.save(type);

        dto.setId(type.getId());
        return dto;
    }
}
