package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByNameIgnoreCase(String name);
}
