package com.example.serviceassistantbackend.dto.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponseDTO {
    private Long id;
    private String vehicleNumber;
    private Long ownerId;
    private String ownerName;
    private Long modelId;
    private String modelName;
}
