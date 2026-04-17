package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MechanicAssignedJobCardsWrapperDTO {
    private List<MechanicAssignedJobCardResponseDTO> jobs;
}
