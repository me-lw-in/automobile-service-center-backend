package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobProblemRepository extends JpaRepository<JobProblem, Long> {
}
