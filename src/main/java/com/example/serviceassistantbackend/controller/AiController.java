package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.ai.AIJobDetailsResponseDTO;
import com.example.serviceassistantbackend.service.ai.AiService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ai")
@AllArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/job-details")
    public ResponseEntity<AIJobDetailsResponseDTO> getJobCardDetails(
            @RequestParam(required = false) String jobCardNumber,
            @RequestParam(required = false) String vehicleNumber) {

        if (jobCardNumber == null && vehicleNumber == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either jobCardNumber or vehicleNumber must be provided");
        }

        AIJobDetailsResponseDTO response =
                aiService.getJobCardDetails(jobCardNumber, vehicleNumber);

        return ResponseEntity.ok(response);
    }
}
