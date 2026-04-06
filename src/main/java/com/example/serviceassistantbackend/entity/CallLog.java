package com.example.serviceassistantbackend.entity;

@lombok.Getter
@lombok.Setter@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "call_logs")
public class CallLog {
@jakarta.persistence.Id
@jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
@jakarta.persistence.Column(name = "id", nullable = false)
private java.lang.Long id;

@jakarta.validation.constraints.Size(max = 15)
@jakarta.persistence.Column(name = "caller_phone", length = 15)
private java.lang.String callerPhone;

@jakarta.persistence.Column(name = "call_arrived_time")
private java.time.Instant callArrivedTime;

@jakarta.persistence.Column(name = "call_ended_time")
private java.time.Instant callEndedTime;

@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
@jakarta.persistence.JoinColumn(name = "job_card_id")
private com.example.serviceassistantbackend.entity.JobCard jobCard;



}