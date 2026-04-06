package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.jobcard.JobCardRequestDTO;
import com.example.serviceassistantbackend.service.jobcrd.JobCardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-cards")
@AllArgsConstructor
public class JobCardController {

    private final JobCardService jobCardService;

    @PostMapping
    public ResponseEntity<String> createJobCard(@RequestBody @Valid JobCardRequestDTO dto){
        jobCardService.createJobCard(dto);
        return ResponseEntity.ok("Job card created successfully");
    }

}
