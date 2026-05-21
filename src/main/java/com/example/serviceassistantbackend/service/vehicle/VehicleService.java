package com.example.serviceassistantbackend.service.vehicle;

import com.example.serviceassistantbackend.dto.vehicle.VehicleRequestDTO;
import com.example.serviceassistantbackend.dto.vehicle.VehicleResponseDTO;
import com.example.serviceassistantbackend.entity.Model;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.entity.Vehicle;
import com.example.serviceassistantbackend.repository.ModelRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import com.example.serviceassistantbackend.repository.VehicleRepository;
import com.example.serviceassistantbackend.repository.JobCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ModelRepository modelRepository;
    private final JobCardRepository jobCardRepository;

    public VehicleResponseDTO createVehicle(VehicleRequestDTO dto) {

        if (vehicleRepository.findByVehicleNumber(dto.getVehicleNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Vehicle already exists");
        }

        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

        Model model = modelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Model not found"));

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(dto.getVehicleNumber().trim().toUpperCase());
        vehicle.setOwner(owner);
        vehicle.setModel(model);
        vehicle.setCreatedAt(LocalDateTime.now());

        vehicleRepository.save(vehicle);

        return mapToResponse(vehicle);
    }

    public VehicleResponseDTO getByVehicleNumber(String number) {
        Vehicle vehicle = vehicleRepository.findByVehicleNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));

        return mapToResponse(vehicle);
    }

    public List<VehicleResponseDTO> getByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public VehicleResponseDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        return mapToResponse(vehicle);
    }

    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO dto) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));

        if (!existing.getVehicleNumber().equalsIgnoreCase(dto.getVehicleNumber()) &&
                vehicleRepository.findByVehicleNumber(dto.getVehicleNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle number already exists");
        }

        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Model model = modelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Model not found"));

        existing.setVehicleNumber(dto.getVehicleNumber().trim().toUpperCase());
        existing.setOwner(owner);
        existing.setModel(model);
        vehicleRepository.save(existing);

        return mapToResponse(existing);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found");
        }

        if (jobCardRepository.existsByVehicleId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete vehicle: associated jobcards exist");
        }

        vehicleRepository.deleteById(id);
    }

    private VehicleResponseDTO mapToResponse(Vehicle vehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId(vehicle.getId());
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        dto.setOwnerId(vehicle.getOwner().getId());
        dto.setOwnerName(vehicle.getOwner().getName());
        dto.setModelId(vehicle.getModel().getId());
        dto.setModelName(vehicle.getModel().getName());
        return dto;
    }
}
