package com.example.serviceassistantbackend.entity;

import com.example.serviceassistantbackend.enums.JobCardStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "job_cards")
public class JobCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "job_card_number", nullable = false, length = 50)
    private String jobCardNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_mechanic_id")
    private User assignedMechanic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", insertable = false)
    private JobCardStatus status;

    @Column(name = "estimated_completion_time")
    private LocalDateTime estimatedCompletionTime;

    @Column(name = "actual_completion_time")
    private LocalDateTime actualCompletionTime;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at",insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ColumnDefault("0.00")
    @Column(name = "current_total_amount", precision = 10, scale = 2)
    private BigDecimal currentTotalAmount;


}