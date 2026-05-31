package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.model.ModelRequestDTO;
import com.example.serviceassistantbackend.entity.Model;
import com.example.serviceassistantbackend.repository.BillRepository;
import com.example.serviceassistantbackend.repository.JobCardRepository;
import com.example.serviceassistantbackend.repository.ModelRepository;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DisplayName("Model CRUD Operations Test Suite")
public class ModelControllerCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JobCardRepository jobCardRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("CREATE - Successfully create a new model")
    void testCreateModel() throws Exception {
        // Arrange
        ModelRequestDTO dto = new ModelRequestDTO();
        dto.setName("Honda Civic");

        // Act & Assert
        mockMvc.perform(
                post("/api/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("Honda Civic")));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve all models")
    void testGetAllModels() throws Exception {
        // Arrange
        Model model1 = new Model();
        model1.setName("Toyota Innova");
        Model model2 = new Model();
        model2.setName("Hyundai Creta");
        modelRepository.save(model1);
        modelRepository.save(model2);

        // Act & Assert
        mockMvc.perform(get("/api/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].name", hasItems("Toyota Innova", "Hyundai Creta")));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve model by ID")
    void testGetModelById() throws Exception {
        // Arrange
        Model model = new Model();
        model.setName("Maruti Swift");
        Model savedModel = modelRepository.save(model);

        // Act & Assert
        mockMvc.perform(get("/api/models/{id}", savedModel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(savedModel.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo("Maruti Swift")));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("UPDATE - Successfully update an existing model")
    void testUpdateModel() throws Exception {
        // Arrange
        Model model = new Model();
        model.setName("OldModelName");
        Model savedModel = modelRepository.save(model);

        ModelRequestDTO updateDTO = new ModelRequestDTO();
        updateDTO.setName("UpdatedModelName");

        // Act & Assert
        mockMvc.perform(
                put("/api/models/{id}", savedModel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(savedModel.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo("UpdatedModelName")));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("DELETE - Successfully delete a model")
    void testDeleteModel() throws Exception {
        // Arrange
        Model model = new Model();
        model.setName("ToDelete");
        Model savedModel = modelRepository.save(model);

        // Act & Assert
        mockMvc.perform(delete("/api/models/{id}", savedModel.getId()))
                .andExpect(status().isOk());

        // Verify deletion - should return 404
        mockMvc.perform(get("/api/models/{id}", savedModel.getId()))
                .andExpect(status().isNotFound());
    }
}
