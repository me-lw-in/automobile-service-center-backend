package com.example.serviceassistantbackend.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must be 50 characters or fewer")
    private String name;
}
