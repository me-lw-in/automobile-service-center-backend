package com.example.serviceassistantbackend.service.problem;

import com.example.serviceassistantbackend.repository.JobProblemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class JobProblemService {

    private final JobProblemRepository jobProblemRepository;

    @Transactional
    public void resolveProblem(Long problemId) {

        var jobProblem = jobProblemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem not found"));

        jobProblem.setResolved(!jobProblem.getResolved());

        jobProblemRepository.save(jobProblem);
    }
}
