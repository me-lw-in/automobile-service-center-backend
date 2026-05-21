package com.example.serviceassistantbackend.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelRequestDTO {
    @NotBlank(message = "Model name is required")
    @Size(max = 100, message = "Model name must be at most 100 characters")
    private String name;
}
