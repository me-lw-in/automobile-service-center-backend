package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "bills")
public class Bill {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "job_card_id", nullable = false)
private com.example.serviceassistantbackend.entity.JobCard jobCard;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
private java.math.BigDecimal totalAmount;

@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "paid")
private java.lang.Boolean paid;

@jakarta.persistence.Column(name = "payment_time")
private java.time.Instant paymentTime;



}