package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.part.PartDTO;
import com.example.serviceassistantbackend.entity.Part;
import com.example.serviceassistantbackend.repository.PartRepository;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DisplayName("Part CRUD Operations Test Suite")
public class PartControllerCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(roles = "SERVICE_MANAGER")
    @DisplayName("CREATE - Successfully create a new part")
    void testCreatePart() throws Exception {
        // Arrange
        PartDTO dto = new PartDTO();
        dto.setName("Brake Pad");
        dto.setPrice(new BigDecimal("1500.00"));
        dto.setStockQuantity(50);

        // Act & Assert
        mockMvc.perform(
                post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("Brake Pad")))
                .andExpect(jsonPath("$.price", equalTo(1500.00)));
    }

    @Test
    @WithMockUser(roles = "SERVICE_MANAGER")
    @DisplayName("READ - Successfully retrieve all parts")
    void testGetAllParts() throws Exception {
        // Arrange
        Part part1 = new Part();
        part1.setName("Oil Filter");
        part1.setPrice(new BigDecimal("300.00"));
        part1.setStockQuantity(100);

        Part part2 = new Part();
        part2.setName("Air Filter");
        part2.setPrice(new BigDecimal("250.00"));
        part2.setStockQuantity(80);

        partRepository.save(part1);
        partRepository.save(part2);

        // Act & Assert
        mockMvc.perform(get("/api/parts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].name", hasItems("Oil Filter", "Air Filter")));
    }

    @Test
    @WithMockUser(roles = "SERVICE_MANAGER")
    @DisplayName("UPDATE - Successfully update an existing part")
    void testUpdatePart() throws Exception {
        // Arrange
        Part part = new Part();
        part.setName("Spark Plug");
        part.setPrice(new BigDecimal("200.00"));
        part.setStockQuantity(120);
        Part savedPart = partRepository.save(part);

        PartDTO updateDTO = new PartDTO();
        updateDTO.setName("Premium Spark Plug");
        updateDTO.setPrice(new BigDecimal("350.00"));
        updateDTO.setStockQuantity(100);

        // Act & Assert
        mockMvc.perform(
                put("/api/parts/{id}", savedPart.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Premium Spark Plug")))
                .andExpect(jsonPath("$.price", equalTo(350.00)));
    }

    @Test
    @WithMockUser(roles = "SERVICE_MANAGER")
    @DisplayName("CREATE - Part validation - negative stock should fail")
    void testCreatePartWithNegativeStock() throws Exception {
        // Arrange
        PartDTO dto = new PartDTO();
        dto.setName("Battery");
        dto.setPrice(new BigDecimal("3000.00"));
        dto.setStockQuantity(-10);

        // Act & Assert
        mockMvc.perform(
                post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isBadRequest());
    }
}
