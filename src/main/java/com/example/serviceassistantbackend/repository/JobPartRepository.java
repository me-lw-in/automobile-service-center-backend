package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Collection;
import java.util.List;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {

    @EntityGraph(attributePaths = {"part"})
    List<JobPart> findByJobCardIdIn(Collection<Long> jobCardIds);

    List<JobPart> findByJobCardId(Long jobCardId);

    boolean existsByJobCardIdAndPartId(Long jobCardId, Long partId);
}
