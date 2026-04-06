package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "models")
public class Model {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false, length = 100)
private java.lang.String name;



}