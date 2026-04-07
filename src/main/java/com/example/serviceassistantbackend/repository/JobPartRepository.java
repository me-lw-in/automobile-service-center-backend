package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {
    boolean existsByJobCardIdAndPartId(Long jobCardId, Long partId);
}
