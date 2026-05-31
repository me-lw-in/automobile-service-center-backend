package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.ai.AIJobDetailsResponseDTO;
import com.example.serviceassistantbackend.service.ai.AiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("AI Controller Test Suite")
public class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiService aiService;

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully get job details by jobCardNumber")
    void testGetJobDetailsByJobCardNumber() throws Exception {
        // Arrange
        AIJobDetailsResponseDTO mockResponse = new AIJobDetailsResponseDTO();
        mockResponse.setJobCardNumber("JC-12345");
        mockResponse.setStatus("IN_PROGRESS");
        
        Mockito.when(aiService.getJobCardDetails("JC-12345", null))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ai/job-details")
                        .param("jobCardNumber", "JC-12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobCardNumber").value("JC-12345"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully get job details by vehicleNumber")
    void testGetJobDetailsByVehicleNumber() throws Exception {
        // Arrange
        AIJobDetailsResponseDTO mockResponse = new AIJobDetailsResponseDTO();
        mockResponse.setJobCardNumber("JC-99999");
        mockResponse.setStatus("COMPLETED");

        Mockito.when(aiService.getJobCardDetails(null, "KA01AB1234"))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/ai/job-details")
                        .param("vehicleNumber", "KA01AB1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobCardNumber").value("JC-99999"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Fail when neither jobCardNumber nor vehicleNumber is provided")
    void testGetJobDetailsFailsWhenNoParams() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/ai/job-details"))
                .andExpect(status().isBadRequest());
    }
}
