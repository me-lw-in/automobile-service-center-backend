package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MechanicAssignedJobProblemDTO {
    private Long id;
    private String problems;
    private Boolean resolved;
}
