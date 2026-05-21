package com.example.serviceassistantbackend.service.jobservices;

import com.example.serviceassistantbackend.dto.service.ServiceDTO;
import com.example.serviceassistantbackend.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobServices {
    private final ServiceRepository serviceRepository;

    public List<ServiceDTO> getAllServices() {
        var services = serviceRepository.findAll();
        List<ServiceDTO> serviceDTOList = new ArrayList<>();
        for (com.example.serviceassistantbackend.entity.Service service : services) {
            var dto = new ServiceDTO();
            dto.setId(service.getId());
            dto.setServiceName(service.getServiceName());
            dto.setPrice(service.getPrice());
            serviceDTOList.add(dto);
        }
        return serviceDTOList;
    }

    public ServiceDTO createService(ServiceDTO dto) {
        var service = new com.example.serviceassistantbackend.entity.Service();
        service.setServiceName(dto.getServiceName().trim());
        service.setPrice(dto.getPrice());
        service = serviceRepository.save(service);

        dto.setId(service.getId());
        return dto;
    }

    public ServiceDTO updateService(Long id, ServiceDTO dto) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
        service.setServiceName(dto.getServiceName().trim());
        service.setPrice(dto.getPrice());
        service = serviceRepository.save(service);

        dto.setId(service.getId());
        return dto;
    }
}
