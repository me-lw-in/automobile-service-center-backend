package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "job_problems")
public class JobProblem {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 255)
@jakarta.persistence.Column(name = "description")
private java.lang.String description;

@org.hibernate.annotations.ColumnDefault("0")
@jakarta.persistence.Column(name = "resolved")
private java.lang.Boolean resolved;

@jakarta.validation.constraints.NotNull
@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY, optional = false)
@org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
@jakarta.persistence.JoinColumn(name = "job_card_id", nullable = false)
private com.example.serviceassistantbackend.entity.JobCard jobCard;



}