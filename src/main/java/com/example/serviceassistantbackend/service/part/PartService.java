package com.example.serviceassistantbackend.service.part;

import com.example.serviceassistantbackend.dto.part.PartDTO;
import com.example.serviceassistantbackend.repository.PartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PartService {

    private final PartRepository partRepository;

    public List<PartDTO> getAllParts() {
        var parts = partRepository.findAll();
        List<PartDTO> partDTOList = new ArrayList<>();
        for (var part : parts) {
            var dto = new PartDTO();
            dto.setId(part.getId());
            dto.setName(part.getName());
            dto.setPrice(part.getPrice());
            dto.setStockQuantity(part.getStockQuantity());
            partDTOList.add(dto);
        }
        return partDTOList;
    }

    public PartDTO createPart(PartDTO dto) {
        var part = new com.example.serviceassistantbackend.entity.Part();
        part.setName(dto.getName().trim());
        part.setPrice(dto.getPrice());
        part.setStockQuantity(dto.getStockQuantity());
        part.setCreatedAt(java.time.LocalDateTime.now());
        part = partRepository.save(part);

        dto.setId(part.getId());
        return dto;
    }

    public PartDTO updatePart(Long id, PartDTO dto) {
        var part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part not found with id: " + id));
        part.setName(dto.getName().trim());
        part.setPrice(dto.getPrice());
        part.setStockQuantity(dto.getStockQuantity());
        part = partRepository.save(part);

        dto.setId(part.getId());
        return dto;
    }
}
