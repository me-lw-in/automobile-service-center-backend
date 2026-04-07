package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.jobcard.JobCardRequestDTO;
import com.example.serviceassistantbackend.dto.jobcard.StartJobRequestDTO;
import com.example.serviceassistantbackend.dto.jobcard.StartJobResponseDTO;
import com.example.serviceassistantbackend.dto.part.AddPartRequestDTO;
import com.example.serviceassistantbackend.dto.service.AddServiceRequestDTO;
import com.example.serviceassistantbackend.service.jobcard.JobCardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/start")
    public ResponseEntity<StartJobResponseDTO> startJob(
            @RequestBody @Valid StartJobRequestDTO request) {

        StartJobResponseDTO response = jobCardService.startJob(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{jobCardId}/services")
    public ResponseEntity<Void> addService(@PathVariable Long jobCardId, @RequestBody @Valid AddServiceRequestDTO dto) {
        jobCardService.addService(jobCardId, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{jobCardId}/parts")
    public ResponseEntity<Void> addService(@PathVariable Long jobCardId, @RequestBody @Valid AddPartRequestDTO dto) {
        jobCardService.addPart(jobCardId, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{jobCardId}/complete")
    public ResponseEntity<String> completeJob(@PathVariable Long jobCardId) {
        jobCardService.completeJob(jobCardId);
        return ResponseEntity.ok("Bill generated successfully");
    }

}
