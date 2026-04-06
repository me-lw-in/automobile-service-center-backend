package com.example.serviceassistantbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Vehicle extends JpaRepository<com.example.serviceassistantbackend.entity.Vehicle, Long> {
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByOwnerId(Long ownerId);
}
