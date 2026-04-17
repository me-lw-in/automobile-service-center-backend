package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Collection;
import java.util.List;

public interface JobServiceRepository extends JpaRepository<JobService, Long> {

    @EntityGraph(attributePaths = {"service"})
    List<JobService> findByJobCardIdIn(Collection<Long> jobCardIds);

    List<JobService> findByJobCardId(Long jobCardId);

    boolean existsByJobCardIdAndServiceId(Long jobCardId, Long serviceId);
}
