package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MechanicAssignedJobServiceDTO {
    private Long id;
    private String serviceName;
    private Boolean completed;
}
