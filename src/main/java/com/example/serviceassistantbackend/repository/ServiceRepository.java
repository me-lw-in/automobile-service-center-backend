package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
