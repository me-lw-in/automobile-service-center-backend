package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "job_services")
public class JobService {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
@jakarta.persistence.JoinColumn(name = "job_card_id", nullable = false)
private com.example.serviceassistantbackend.entity.JobCard jobCard;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "service_id", nullable = false)
private com.example.serviceassistantbackend.entity.Service service;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "performed_by", nullable = false)
private com.example.serviceassistantbackend.entity.User performedBy;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "price_at_time", nullable = false, precision = 10, scale = 2)
private java.math.BigDecimal priceAtTime;

@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "completed")
private java.lang.Boolean completed;



}