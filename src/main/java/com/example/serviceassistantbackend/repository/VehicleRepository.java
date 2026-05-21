package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.Model;
import com.example.serviceassistantbackend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByOwnerId(Long ownerId);

    long countByModel(Model model);

    boolean existsByOwnerId(Long ownerId);
}
