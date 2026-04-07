package com.example.serviceassistantbackend.dto.part;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPartRequestDTO {

    @NotNull(message = "Part id is required")
    private Long partId;

    @NotNull(message = "quantity is required")
    private Integer quantity;
}
