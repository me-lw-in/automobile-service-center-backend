package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobCard;
import com.example.serviceassistantbackend.enums.JobCardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface JobCardRepository extends JpaRepository<JobCard, Long> {

    boolean existsByVehicleIdAndStatusIn(Long vehicleId, Collection<JobCardStatus> statuses);
}
