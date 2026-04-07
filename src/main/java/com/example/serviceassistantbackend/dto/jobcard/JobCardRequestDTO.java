package com.example.serviceassistantbackend.dto.jobcard;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class JobCardRequestDTO {
    @NotBlank(message = "Vehicle number is required")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$",
            message = "Invalid vehicle number format (e.g., KA20AB1234)"
    )
    private String vehicleNumber;

    @NotNull(message = "Service type is required")
    private Long serviceTypeId;

    private List<String> problems;
}
