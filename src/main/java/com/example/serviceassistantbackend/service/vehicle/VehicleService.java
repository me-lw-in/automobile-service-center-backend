package com.example.serviceassistantbackend.service.vehicle;

import com.example.serviceassistantbackend.dto.vehicle.VehicleRequestDTO;
import com.example.serviceassistantbackend.dto.vehicle.VehicleResponseDTO;
import com.example.serviceassistantbackend.entity.Model;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.entity.Vehicle;
import com.example.serviceassistantbackend.repository.ModelRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import com.example.serviceassistantbackend.repository.VehicleRepository;
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

    private VehicleResponseDTO mapToResponse(Vehicle vehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId(vehicle.getId());
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        dto.setOwnerName(vehicle.getOwner().getName());
        dto.setModelName(vehicle.getModel().getName());
        return dto;
    }
}
