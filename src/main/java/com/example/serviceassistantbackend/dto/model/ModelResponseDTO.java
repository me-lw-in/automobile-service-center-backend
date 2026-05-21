package com.example.serviceassistantbackend.dto.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelResponseDTO {
    private Long id;
    private String name;
    private boolean hasAssociatedVehicles;
}
