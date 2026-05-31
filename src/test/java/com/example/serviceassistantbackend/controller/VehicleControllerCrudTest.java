package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.vehicle.VehicleRequestDTO;
import com.example.serviceassistantbackend.entity.Model;
import com.example.serviceassistantbackend.entity.Role;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.entity.Vehicle;
import com.example.serviceassistantbackend.repository.ModelRepository;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import com.example.serviceassistantbackend.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DisplayName("Vehicle CRUD Operations Test Suite")
public class VehicleControllerCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testOwner;
    private Model testModel;

    @BeforeEach
    void setUp() {


        // Create test role, user, and model
        Role role = new Role();
        role.setName("Customer_" + System.currentTimeMillis());
        Role savedRole = roleRepository.save(role);

        testOwner = new User();
        testOwner.setName("John Doe");
        testOwner.setPhone("9876543210");
        testOwner.setEmail("john@example.com");
        testOwner.setPassword("hashed_password");
        testOwner.setRole(savedRole);
        testOwner = userRepository.save(testOwner);

        testModel = new Model();
        testModel.setName("Honda Civic");
        testModel = modelRepository.save(testModel);
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("CREATE - Successfully create a new vehicle")
    void testCreateVehicle() throws Exception {
        // Arrange
        VehicleRequestDTO dto = new VehicleRequestDTO();
        dto.setVehicleNumber("KA01AB1234");
        dto.setOwnerId(testOwner.getId());
        dto.setModelId(testModel.getId());

        // Act & Assert
        MvcResult result = mockMvc.perform(
                post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.vehicleNumber", equalTo("KA01AB1234")))
                .andReturn();
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve all vehicles")
    void testGetAllVehicles() throws Exception {
        // Arrange
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleNumber("KA01AB1234");
        vehicle1.setOwner(testOwner);
        vehicle1.setModel(testModel);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleNumber("KA02CD5678");
        vehicle2.setOwner(testOwner);
        vehicle2.setModel(testModel);

        vehicleRepository.save(vehicle1);
        vehicleRepository.save(vehicle2);

        // Act & Assert
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].vehicleNumber", hasItems("KA01AB1234", "KA02CD5678")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve vehicle by ID")
    void testGetVehicleById() throws Exception {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA03EF9012");
        vehicle.setOwner(testOwner);
        vehicle.setModel(testModel);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Act & Assert
        mockMvc.perform(get("/api/vehicles/{id}", savedVehicle.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber", equalTo("KA03EF9012")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("UPDATE - Successfully update an existing vehicle")
    void testUpdateVehicle() throws Exception {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA04GH3456");
        vehicle.setOwner(testOwner);
        vehicle.setModel(testModel);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        VehicleRequestDTO updateDTO = new VehicleRequestDTO();
        updateDTO.setVehicleNumber("KA04GH9999");
        updateDTO.setOwnerId(testOwner.getId());
        updateDTO.setModelId(testModel.getId());

        // Act & Assert
        mockMvc.perform(
                put("/api/vehicles/{id}", savedVehicle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber", equalTo("KA04GH9999")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("DELETE - Successfully delete a vehicle")
    void testDeleteVehicle() throws Exception {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA05IJ7890");
        vehicle.setOwner(testOwner);
        vehicle.setModel(testModel);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Act & Assert
        mockMvc.perform(delete("/api/vehicles/{id}", savedVehicle.getId()))
                .andExpect(status().isOk());

        // Verify deletion
        mockMvc.perform(get("/api/vehicles/{id}", savedVehicle.getId()))
                .andExpect(status().isNotFound());
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Get vehicle by vehicle number")
    void testGetVehicleByNumber() throws Exception {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA06KL1111");
        vehicle.setOwner(testOwner);
        vehicle.setModel(testModel);
        vehicleRepository.save(vehicle);

        // Act & Assert
        mockMvc.perform(get("/api/vehicles/number/KA06KL1111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber", equalTo("KA06KL1111")));
    }
}
