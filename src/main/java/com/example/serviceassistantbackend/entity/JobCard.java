package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "job_cards")
public class JobCard {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 50)
@jakarta.validation.constraints.NotNull
@jakarta.persistence.Column(name = "job_card_number", nullable = false, length = 50)
private java.lang.String jobCardNumber;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "vehicle_id", nullable = false)
private com.example.serviceassistantbackend.entity.Vehicle vehicle;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "service_type_id")
private com.example.serviceassistantbackend.entity.ServiceType serviceType;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@jakarta.persistence.JoinColumn(name = "created_by", nullable = false)
private com.example.serviceassistantbackend.entity.User createdBy;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "assigned_mechanic_id")
private com.example.serviceassistantbackend.entity.User assignedMechanic;

@org.hibernate.annotations.ColumnDefault("'CREATED'")
@jakarta.persistence.Lob
@jakarta.persistence.Column(name = "status")
private java.lang.String status;

@jakarta.persistence.Column(name = "estimated_completion_time")
private java.time.Instant estimatedCompletionTime;

@jakarta.persistence.Column(name = "actual_completion_time")
private java.time.Instant actualCompletionTime;

@org.hibernate.annotations.ColumnDefault("CURRENT_TIMESTAMP")
@jakarta.persistence.Column(name = "created_at")
private java.time.Instant createdAt;



}