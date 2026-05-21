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

    @GetMapping
    public List<VehicleResponseDTO> getAll() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public VehicleResponseDTO getById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PutMapping("/{id}")
    public VehicleResponseDTO update(@PathVariable Long id, @RequestBody @Valid VehicleRequestDTO dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
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
