package com.example.serviceassistantbackend.service.jobcard;

import com.example.serviceassistantbackend.dto.jobcard.JobCardRequestDTO;
import com.example.serviceassistantbackend.dto.jobcard.StartJobRequestDTO;
import com.example.serviceassistantbackend.dto.jobcard.StartJobResponseDTO;
import com.example.serviceassistantbackend.dto.part.AddPartRequestDTO;
import com.example.serviceassistantbackend.dto.service.AddServiceRequestDTO;
import com.example.serviceassistantbackend.entity.*;
import com.example.serviceassistantbackend.enums.JobCardStatus;
import com.example.serviceassistantbackend.repository.*;
import com.example.serviceassistantbackend.util.JobCardNumberUtil;
import com.example.serviceassistantbackend.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class JobCardService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final VehicleRepository vehicleRepository;
    private final JobCardRepository jobCardRepository;
    private final ServiceRepository serviceRepository;
    private final JobPartRepository jobPartRepository;
    private final JobProblemRepository jobProblemRepository;
    private final JobServiceRepository jobServiceRepository;
    private final ServiceTypeRepository serviceTypeRepository;


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
        if (jobCardRepository.existsByVehicleIdAndStatusNotIn(vehicle.getId(), List.of(JobCardStatus.CREATED, JobCardStatus.IN_PROGRESS))){
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

    public StartJobResponseDTO startJob(StartJobRequestDTO request) {

        // 1. Fetch Job Card
        JobCard jobCard = jobCardRepository.findById(request.getJobCardId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Job card not found"));

        // 2. Fetch Mechanic
        var mechanicId = SecurityUtil.getCurrentUserId();
        if (mechanicId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }
        var mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Mechanic not found"));

        // 3. Update status + assign mechanic
        jobCard.setStatus(JobCardStatus.IN_PROGRESS);
        jobCard.setAssignedMechanic(mechanic);
        jobCard.setEstimatedCompletionTime(request.getEstimatedCompletionTime());

        jobCardRepository.save(jobCard);

        // 4. Get problems (if any)
        var problems = jobProblemRepository.findByJobCardId(jobCard.getId());

        List<String> problemList = problems.stream()
                .map(JobProblem::getDescription)
                .toList();

        // 5. Build response
        return new StartJobResponseDTO(
                jobCard.getId(),
                jobCard.getVehicle().getVehicleNumber(),
                jobCard.getServiceType().getName(), // adjust based on your entity
                problemList,
                jobCard.getStatus().name()
        );
    }

    @Transactional
    public void addService(Long jobCardId, AddServiceRequestDTO dto) {
        var mechanicId = SecurityUtil.getCurrentUserId();
        if (mechanicId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }
        var mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Mechanic not found"));

        if (jobServiceRepository.existsByJobCardIdAndServiceId(jobCardId, dto.getServiceId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create duplicate service.");
        }

        var jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Job card not found"));

        if (jobCard.getStatus() != JobCardStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle service is not yet started.");
        }

        var service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Service not found"));

        // 1. Save job service
        JobService jobService = new JobService();
        jobService.setJobCard(jobCard);
        jobService.setService(service);
        jobService.setPerformedBy(mechanic);
        jobService.setPriceAtTime(service.getPrice());
        jobService.setCompleted(true);
        jobServiceRepository.save(jobService);

        // 2. Update total amount
        jobCard.setCurrentTotalAmount(jobCard.getCurrentTotalAmount().add(service.getPrice()));
        jobCardRepository.save(jobCard);
    }

    @Transactional
    public void addPart(Long jobCardId, AddPartRequestDTO dto) {

        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Job card not found"));

        if (jobCard.getStatus() != JobCardStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle service is not yet started.");
        }

        if (jobPartRepository.existsByJobCardIdAndPartId(jobCardId,dto.getPartId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add duplicate parts. Please update the quantity.");
        }

        Part part = partRepository.findById(dto.getPartId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Part not found"));

        // 🔴 Check inventory
        if (part.getStockQuantity() < dto.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough stock");
        }

        // 1. Reduce inventory
        part.setStockQuantity(part.getStockQuantity() - dto.getQuantity());
        partRepository.save(part);

        // 2. Save job part
        var jobPart = new JobPart();
        jobPart.setJobCard(jobCard);
        jobPart.setPart(part);
        jobPart.setQuantity(dto.getQuantity());
        jobPart.setPriceAtTime(part.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
        jobPartRepository.save(jobPart);

        // 3. Update total amount
        jobCard.setCurrentTotalAmount(jobCard.getCurrentTotalAmount().add(part.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()))));
        jobCardRepository.save(jobCard);
    }

    @Transactional
    public void completeJob(Long jobCardId) {

        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Job card not found"));

        if (billRepository.existsByJobCardId(jobCardId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bill is already generated for this jobCard");
        }

        if (jobCard.getStatus() != JobCardStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle service is not yet started.");
        }

        // 🔴 Optional: check all problems resolved
        boolean unresolvedExists = jobProblemRepository.existsByJobCardIdAndResolvedFalse(jobCardId);

        if (unresolvedExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Resolve all problems before completing job");
        }

        // 1. Update status
        jobCard.setStatus(JobCardStatus.COMPLETED);
        jobCard.setActualCompletionTime(LocalDateTime.now());
        jobCardRepository.save(jobCard);

        // 2. Create bill

        var bill = new Bill();
        bill.setJobCard(jobCard);
        bill.setTotalAmount(jobCard.getCurrentTotalAmount());
        billRepository.save(bill);

    }
}
