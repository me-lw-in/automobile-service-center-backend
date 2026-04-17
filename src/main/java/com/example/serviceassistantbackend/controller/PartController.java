package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.part.PartDTO;
import com.example.serviceassistantbackend.service.part.PartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<List<PartDTO>> getAllParts() {
        return ResponseEntity.ok(partService.getAllParts());
    }
}
