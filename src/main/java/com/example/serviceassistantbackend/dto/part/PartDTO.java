package com.example.serviceassistantbackend.dto.part;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
}
