package com.example.serviceassistantbackend.dto.ai;

import com.example.serviceassistantbackend.dto.part.AiResponsePartDTO;
import com.example.serviceassistantbackend.dto.service.AiResponseServiceDTO;
import com.example.serviceassistantbackend.dto.vehicle.AiVehicleResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AIJobDetailsResponseDTO {
    private String jobCardNumber;
    private String status;
    private BigDecimal currentTotalAmount;

    private LocalDateTime estimatedCompletionTime;
    private LocalDateTime actualCompletionTime;
    private LocalDateTime createdAt;

    private AiVehicleResponseDTO vehicle;
    private List<AiResponsePartDTO> parts;
    private List<AiResponseServiceDTO> services;
}
