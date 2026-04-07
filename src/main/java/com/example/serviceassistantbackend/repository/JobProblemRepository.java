package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobProblemRepository extends JpaRepository<JobProblem, Long> {
    List<JobProblem> findByJobCardId(Long jobCardId);

    boolean existsByJobCardIdAndResolvedFalse(Long jobCardId);
}
