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
}
