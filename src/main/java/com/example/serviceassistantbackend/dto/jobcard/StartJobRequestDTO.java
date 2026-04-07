package com.example.serviceassistantbackend.dto.jobcard;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StartJobRequestDTO {

    @NotNull(message = "Job card ID is required")
    private Long jobCardId;


    @NotNull(message = "Estimated completion time is required")
    @Future(message = "Estimated completion time must be in the future")
    private LocalDateTime estimatedCompletionTime;

}
