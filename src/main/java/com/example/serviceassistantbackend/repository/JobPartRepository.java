package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {

    List<JobPart> findByJobCardId(Long jobCardId);

    boolean existsByJobCardIdAndPartId(Long jobCardId, Long partId);
}
