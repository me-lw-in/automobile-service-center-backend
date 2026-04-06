package com.example.serviceassistantbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "call_logs")
public class CallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 15)
    @Column(name = "caller_phone", length = 15)
    private String callerPhone;

    @Column(name = "call_arrived_time")
    private LocalDateTime callArrivedTime;

    @Column(name = "call_ended_time")
    private LocalDateTime callEndedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_card_id")
    private JobCard jobCard;


}