package com.example.serviceassistantbackend.dto.service;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AiResponseServiceDTO {
    private String serviceName;
    private BigDecimal price;
    private Boolean completed;
}
