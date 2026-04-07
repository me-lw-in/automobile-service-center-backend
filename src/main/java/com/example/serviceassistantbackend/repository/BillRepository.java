package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
    boolean existsByJobCardId(Long jobCardId);
}
