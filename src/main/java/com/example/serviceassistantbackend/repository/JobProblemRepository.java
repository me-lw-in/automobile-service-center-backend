package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Collection;
import java.util.List;

public interface JobProblemRepository extends JpaRepository<JobProblem, Long> {
    List<JobProblem> findByJobCardId(Long jobCardId);

    @EntityGraph(attributePaths = {"jobCard"})
    List<JobProblem> findByJobCardIdIn(Collection<Long> jobCardIds);

    boolean existsByJobCardIdAndResolvedFalse(Long jobCardId);
}
