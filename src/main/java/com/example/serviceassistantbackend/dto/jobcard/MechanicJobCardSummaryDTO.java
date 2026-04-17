package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MechanicJobCardSummaryDTO {
    private Long id;
    private String jobCardNumber;
    private String vehicleModel;
    private String serviceName;
    private List<MechanicJobProblemDTO> jobProblems;
    private String status;
}
