package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "services")
public class Service {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "service_name", nullable = false, length = 100)
private java.lang.String serviceName;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "price", nullable = false, precision = 10, scale = 2)
private java.math.BigDecimal price;



}