package com.example.serviceassistantbackend.repository;

import com.example.serviceassistantbackend.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {
}
