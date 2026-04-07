package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.service.problem.JobProblemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-problems")
@AllArgsConstructor
public class JobProblemController {

    private final JobProblemService jobProblemService;

    @PatchMapping("{problemId}/resolve")
    public ResponseEntity<Void> resolveProblem(@PathVariable Long problemId) {
        jobProblemService.resolveProblem(problemId);
        return ResponseEntity.noContent().build();
    }

}
