package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.DashboardStatsDTO;
import com.example.serviceassistantbackend.entity.JobCard;
import com.example.serviceassistantbackend.entity.Role;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.entity.Vehicle;
import com.example.serviceassistantbackend.enums.JobCardStatus;
import com.example.serviceassistantbackend.repository.JobCardRepository;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import com.example.serviceassistantbackend.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JobCardRepository jobCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        // Total vehicles
        Long totalVehicles = (long) vehicleRepository.findAll().size();

        // Get all job cards
        List<JobCard> allJobCards = jobCardRepository.findAll();

        // Vehicles in progress (unique vehicles with IN_PROGRESS status)
        Set<Long> vehiclesInProgressIds = allJobCards.stream()
                .filter(jc -> jc.getStatus() == JobCardStatus.IN_PROGRESS)
                .map(jc -> jc.getVehicle().getId())
                .collect(Collectors.toSet());
        Long vehiclesInProgress = (long) vehiclesInProgressIds.size();

        // Vehicles ready for delivery (unique vehicles with COMPLETED status, not yet delivered)
        Set<Long> vehiclesReadyIds = allJobCards.stream()
                .filter(jc -> jc.getStatus() == JobCardStatus.COMPLETED)
                .map(jc -> jc.getVehicle().getId())
                .collect(Collectors.toSet());
        Long vehiclesReadyForDelivery = (long) vehiclesReadyIds.size();

        // Total unique vehicles whose job card is created (from job cards table)
        Set<Long> vehiclesWithJobCardIds = allJobCards.stream()
                .map(jc -> jc.getVehicle().getId())
                .collect(Collectors.toSet());
        Long totalVehiclesWithJobCard = (long) vehiclesWithJobCardIds.size();

        // Total users
        Long totalUsers = (long) userRepository.findAll().size();

        // Get role IDs for counting
        Role mechanicRole = roleRepository.findByName("MECHANIC").orElse(null);
        Role serviceInchargeRole = roleRepository.findByName("SERVICE_INCHARGE").orElse(null);
        Role serviceManagerRole = roleRepository.findByName("SERVICE_MANAGER").orElse(null);
        Role customerRole = roleRepository.findByName("CUSTOMER").orElse(null);

        // Count users by role
        List<User> allUsers = userRepository.findAll();
        
        Long totalMechanics = allUsers.stream()
                .filter(u -> mechanicRole != null && u.getRole().getId().equals(mechanicRole.getId()))
                .count();
        
        Long totalServiceIncharge = allUsers.stream()
                .filter(u -> serviceInchargeRole != null && u.getRole().getId().equals(serviceInchargeRole.getId()))
                .count();
        
        Long totalServiceManager = allUsers.stream()
                .filter(u -> serviceManagerRole != null && u.getRole().getId().equals(serviceManagerRole.getId()))
                .count();
        
        Long totalCustomers = allUsers.stream()
                .filter(u -> customerRole != null && u.getRole().getId().equals(customerRole.getId()))
                .count();

        DashboardStatsDTO stats = new DashboardStatsDTO(
                totalVehicles,
                vehiclesInProgress,
                vehiclesReadyForDelivery,
                totalUsers,
                totalMechanics,
                totalServiceIncharge,
                totalServiceManager,
                totalCustomers,
                totalVehiclesWithJobCard
        );

        return ResponseEntity.ok(stats);
    }
}
