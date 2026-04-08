package com.example.serviceassistantbackend.dto.part;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AiResponsePartDTO {
    private String partName; // from part table
    private Integer quantity;
    private BigDecimal price;
}
