package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "service_types")
public class ServiceType {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false, length = 100)
private java.lang.String name;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.persistence.Column(name = "description")
private java.lang.String description;



}