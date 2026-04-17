package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobCard;
import com.example.serviceassistantbackend.enums.JobCardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JobCardRepository extends JpaRepository<JobCard, Long> {

    Optional<JobCard> findByJobCardNumber(String jobCardNumber);

    @Query("""
        SELECT jc
        FROM JobCard jc
        JOIN jc.vehicle v
        WHERE v.vehicleNumber = :vehicleNumber
        ORDER BY jc.createdAt DESC
    """)
    List<JobCard> findLatestByVehicleNumber(@Param("vehicleNumber") String vehicleNumber);

    @EntityGraph(attributePaths = {"vehicle", "vehicle.model", "serviceType"})
    Page<JobCard> findByStatusOrderByCreatedAtAsc(JobCardStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"vehicle", "vehicle.model"})
    List<JobCard> findByAssignedMechanicIdOrderByCreatedAtDesc(Long assignedMechanicId);

    @EntityGraph(attributePaths = {"vehicle", "vehicle.model"})
    List<JobCard> findByAssignedMechanicIdAndStatusOrderByCreatedAtDesc(Long assignedMechanicId, JobCardStatus status);

    @EntityGraph(attributePaths = {"vehicle", "vehicle.owner", "serviceType"})
    List<JobCard> findByCreatedByIdOrderByCreatedAtDesc(Long createdById);

    boolean existsByVehicleIdAndStatusIn(Long vehicleId, Collection<JobCardStatus> statuses);
}
