package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
}
