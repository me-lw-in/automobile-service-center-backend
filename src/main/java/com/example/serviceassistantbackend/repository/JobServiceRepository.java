package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobCard;
import com.example.serviceassistantbackend.entity.JobService;
import com.example.serviceassistantbackend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobServiceRepository extends JpaRepository<JobService, Long> {
    boolean existsByJobCardAndService(JobCard jobCard, Service service);

    boolean existsByJobCardIdAndServiceId(Long jobCardId, Long serviceId);
}
