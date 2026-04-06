package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "users")
public class User {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false)
private java.lang.String name;

@jakarta.validation.constraints.Size(max = 15)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "phone", nullable = false, length = 15)
private java.lang.String phone;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "role_id", nullable = false)
private com.example.serviceassistantbackend.entity.Role role;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "created_at")
private java.time.Instant createdAt;



}