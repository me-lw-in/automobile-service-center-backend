package com.example.serviceassistantbackend.service.ai;

import com.example.serviceassistantbackend.dto.ai.AIJobDetailsResponseDTO;
import com.example.serviceassistantbackend.dto.part.AiResponsePartDTO;
import com.example.serviceassistantbackend.dto.service.AiResponseServiceDTO;
import com.example.serviceassistantbackend.dto.vehicle.AiVehicleResponseDTO;
import com.example.serviceassistantbackend.entity.*;
import com.example.serviceassistantbackend.repository.JobCardRepository;
import com.example.serviceassistantbackend.repository.JobPartRepository;
import com.example.serviceassistantbackend.repository.JobServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class AiService {

    private final JobCardRepository jobCardRepository;
    private final JobPartRepository jobPartRepository;
    private final JobServiceRepository jobServiceRepository;

    public AIJobDetailsResponseDTO getJobCardDetails(String jobCardNumber, String vehicleNumber) {

        JobCard jobCard;

        // ✅ Step 1: Fetch JobCard
        if (jobCardNumber != null) {
            jobCard = jobCardRepository.findByJobCardNumber(jobCardNumber)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job card not found"));
        } else {
            List<JobCard> jobCards =
                    jobCardRepository.findLatestByVehicleNumber(vehicleNumber);

            if (jobCards.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No job cards found for vehicle");
            }

            jobCard = jobCards.getFirst(); // latest
        }

        // ✅ Step 2: Fetch Parts & Services
        List<JobPart> parts = jobPartRepository.findByJobCardId(jobCard.getId());
        List<JobService> services = jobServiceRepository.findByJobCardId(jobCard.getId());

        // ✅ Step 3: Map to DTO
        return mapToDTO(jobCard, parts, services);
    }

    private AIJobDetailsResponseDTO mapToDTO(
            JobCard jobCard,
            List<JobPart> parts,
            List<JobService> services) {

        AIJobDetailsResponseDTO dto = new AIJobDetailsResponseDTO();

        dto.setJobCardNumber(jobCard.getJobCardNumber());
        dto.setStatus(jobCard.getStatus().name());
        dto.setCurrentTotalAmount(jobCard.getCurrentTotalAmount());
        dto.setEstimatedCompletionTime(jobCard.getEstimatedCompletionTime());
        dto.setActualCompletionTime(jobCard.getActualCompletionTime());
        dto.setCreatedAt(jobCard.getCreatedAt());

        // ✅ Vehicle + Owner
        Vehicle vehicle = jobCard.getVehicle();
        User owner = vehicle.getOwner();

        AiVehicleResponseDTO vehicleDTO = new AiVehicleResponseDTO();
        vehicleDTO.setVehicleNumber(vehicle.getVehicleNumber());
        vehicleDTO.setOwnerName(owner.getName());
        vehicleDTO.setOwnerPhone(owner.getPhone());

        dto.setVehicle(vehicleDTO);

        // ✅ Parts
        List<AiResponsePartDTO> partDTOs = parts.stream().map(p -> {
            AiResponsePartDTO partDTO = new AiResponsePartDTO();
            partDTO.setPartName(p.getPart().getName());
            partDTO.setQuantity(p.getQuantity());
            partDTO.setPrice(p.getPriceAtTime());
            return partDTO;
        }).toList();

        dto.setParts(partDTOs);

        // ✅ Services
        List<AiResponseServiceDTO> serviceDTOs = services.stream().map(s -> {
            AiResponseServiceDTO serviceDTO = new AiResponseServiceDTO();
            serviceDTO.setServiceName(s.getService().getServiceName());
            serviceDTO.setPrice(s.getPriceAtTime());
            serviceDTO.setCompleted(s.getCompleted());
            return serviceDTO;
        }).toList();

        dto.setServices(serviceDTOs);

        return dto;
    }

}
