package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "roles")
public class Role {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 50)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false, length = 50)
private java.lang.String name;



}