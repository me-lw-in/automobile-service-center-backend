package com.example.serviceassistantbackend.dto.service;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddServiceRequestDTO {
    @NotNull(message = "Service Id is required")
    private Long serviceId;
}
