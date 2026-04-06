package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.vehicle.VehicleRequestDTO;
import com.example.serviceassistantbackend.dto.vehicle.VehicleResponseDTO;
import com.example.serviceassistantbackend.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@AllArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping()
    public VehicleResponseDTO create(@RequestBody @Valid VehicleRequestDTO dto) {
        return vehicleService.createVehicle(dto);
    }

    @GetMapping("/number/{vehicleNumber}")
    public VehicleResponseDTO getByNumber(@PathVariable String vehicleNumber) {
        return vehicleService.getByVehicleNumber(vehicleNumber);
    }

    @GetMapping("/owner/{ownerId}")
    public List<VehicleResponseDTO> getByOwner(@PathVariable Long ownerId) {
        return vehicleService.getByOwner(ownerId);
    }
}
