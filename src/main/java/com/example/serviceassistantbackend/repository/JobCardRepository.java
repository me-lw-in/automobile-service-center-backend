package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.JobCard;
import com.example.serviceassistantbackend.enums.JobCardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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

    boolean existsByVehicleIdAndStatusNotIn(Long vehicleId, Collection<JobCardStatus> statuses);
}
