package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MechanicAssignedJobCardResponseDTO {
    private Long jobCardId;
    private String jobCardNumber;
    private String vehicleModel;
    private String serviceName;
    private BigDecimal currentTotalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedCompletionTime;
    private LocalDateTime actualCompletionTime;
    private String status;
    private List<MechanicAssignedJobServiceDTO> jobServices;
    private List<MechanicAssignedJobPartDTO> jobParts;
    private List<MechanicAssignedJobProblemDTO> jobProblems;
}
