package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobServiceRepository extends JpaRepository<JobService, Long> {

    List<JobService> findByJobCardId(Long jobCardId);

    boolean existsByJobCardIdAndServiceId(Long jobCardId, Long serviceId);
}
