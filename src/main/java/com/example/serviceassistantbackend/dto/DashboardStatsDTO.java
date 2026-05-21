package com.example.serviceassistantbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalVehicles;
    private Long vehiclesInProgress;
    private Long vehiclesReadyForDelivery;
    private Long totalUsers;
    private Long totalMechanics;
    private Long totalServiceIncharge;
    private Long totalServiceManager;
    private Long totalCustomers;
    private Long totalVehiclesWithJobCard;
}
