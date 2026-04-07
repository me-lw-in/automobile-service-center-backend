package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StartJobResponseDTO {
    private Long jobCardId;
    private String vehicleNumber;
    private String serviceType;
    private List<String> problems;
    private String status;
}
