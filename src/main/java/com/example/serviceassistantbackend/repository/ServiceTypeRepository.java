package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
}
