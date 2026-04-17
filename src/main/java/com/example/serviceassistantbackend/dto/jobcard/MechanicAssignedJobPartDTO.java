package com.example.serviceassistantbackend.dto.jobcard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MechanicAssignedJobPartDTO {
    private Long id;
    private String partName;
    private Integer quantity;
    private BigDecimal totalPrice;
}
