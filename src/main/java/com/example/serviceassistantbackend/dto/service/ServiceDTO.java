package com.example.serviceassistantbackend.dto.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDTO {
    private Long id;

    private String serviceName;

    private BigDecimal price;
}
