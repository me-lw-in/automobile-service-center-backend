package com.example.serviceassistantbackend.service.jobcrd;

import com.example.serviceassistantbackend.dto.jobcard.JobCardRequestDTO;
import com.example.serviceassistantbackend.entity.JobCard;
import com.example.serviceassistantbackend.entity.JobProblem;
import com.example.serviceassistantbackend.enums.JobCardStatus;
import com.example.serviceassistantbackend.repository.*;
import com.example.serviceassistantbackend.util.JobCardNumberUtil;
import com.example.serviceassistantbackend.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@AllArgsConstructor
public class JobCardService {
    private final VehicleRepository vehicleRepository;
    private final JobProblemRepository jobProblemRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final UserRepository userRepository;
    private final JobCardRepository jobCardRepository;

    @Transactional
    public void createJobCard(JobCardRequestDTO dto) {
        var userId = SecurityUtil.getCurrentUserId();
        if (userId == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized.");
        }
        var vehicle = vehicleRepository.findByVehicleNumber(dto.getVehicleNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle not found"));

        var serviceType = serviceTypeRepository.findById(dto.getServiceTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Service type not found"));
        if (jobCardRepository.existsByVehicleIdAndStatusIn(vehicle.getId(), List.of(JobCardStatus.CREATED, JobCardStatus.IN_PROGRESS))){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already a job card is created");
        }

        var user = userRepository.getReferenceById(userId);

        var jobCard = new JobCard();
        jobCard.setJobCardNumber(JobCardNumberUtil.generateJobCardNumber());
        jobCard.setVehicle(vehicle);
        jobCard.setServiceType(serviceType);
        jobCard.setCreatedBy(user);
        jobCard.setCurrentTotalAmount(serviceType.getPrice());


        jobCardRepository.save(jobCard);

        // Save complaints if any
        if (dto.getProblems() != null && !dto.getProblems().isEmpty()) {
            for (String p : dto.getProblems()) {
                JobProblem problem = new JobProblem();
                problem.setDescription(p);
                problem.setJobCard(jobCard);
                jobProblemRepository.save(problem);
            }
        }

    }
}
