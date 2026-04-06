package com.example.serviceassistantbackend.dto.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponseDTO {
    private Long id;
    private String vehicleNumber;
    private String ownerName;
    private String modelName;
}
