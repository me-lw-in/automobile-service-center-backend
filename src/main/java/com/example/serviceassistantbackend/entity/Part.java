package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "parts")
public class Part {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 100)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "name", nullable = false, length = 100)
private java.lang.String name;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "price", nullable = false, precision = 10, scale = 2)
private java.math.BigDecimal price;

@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "stock_quantity")
private java.lang.Integer stockQuantity;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "created_at")
private java.time.Instant createdAt;



}