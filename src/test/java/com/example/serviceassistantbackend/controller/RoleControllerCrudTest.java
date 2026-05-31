package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.role.RoleDTO;
import com.example.serviceassistantbackend.entity.Role;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.serviceassistantbackend.repository.UserRepository;
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
@DisplayName("Role CRUD Operations Test Suite")
public class RoleControllerCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

        @Autowired
        private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("CREATE - Successfully create a new role")
    void testCreateRole() throws Exception {
        // Arrange
        String roleName = "Mechanic_" + System.currentTimeMillis();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(roleName);

        // Act & Assert
        MvcResult result = mockMvc.perform(
                post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo(roleName.toUpperCase())))
                .andReturn();

        Long savedId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        roleRepository.findById(savedId).orElseThrow();
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve all roles")
    void testGetAllRoles() throws Exception {
        // Arrange
        String name1 = "Admin_" + System.currentTimeMillis();
        String name2 = "Mechanic_" + System.currentTimeMillis();
        Role role1 = new Role();
        role1.setName(name1);
        Role role2 = new Role();
        role2.setName(name2);
        roleRepository.save(role1);
        roleRepository.save(role2);

        // Act & Assert
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].name", hasItems(name1, name2)));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve role by ID")
    void testGetRoleById() throws Exception {
        // Arrange
        String roleName = "ServiceIncharge_" + System.currentTimeMillis();
        Role role = new Role();
        role.setName(roleName);
        Role savedRole = roleRepository.save(role);

        // Act & Assert
        mockMvc.perform(get("/api/roles/{id}", savedRole.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(savedRole.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(roleName)));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("UPDATE - Successfully update an existing role")
    void testUpdateRole() throws Exception {
        // Arrange
        Role role = new Role();
        role.setName("OldRole_" + System.currentTimeMillis());
        Role savedRole = roleRepository.save(role);

        String updatedName = "UpdatedRole_" + System.currentTimeMillis();
        RoleDTO updatedDTO = new RoleDTO();
        updatedDTO.setName(updatedName);

        // Act & Assert
        mockMvc.perform(
                put("/api/roles/{id}", savedRole.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(savedRole.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(updatedName.toUpperCase())));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("DELETE - Successfully delete a role")
    void testDeleteRole() throws Exception {
        // Arrange
        String roleName = "ToDelete_" + System.currentTimeMillis();
        Role role = new Role();
        role.setName(roleName);
        Role savedRole = roleRepository.save(role);

        mockMvc.perform(delete("/api/roles/{id}", savedRole.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/roles/{id}", savedRole.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("CREATE - Role validation - empty name should fail")
    void testCreateRoleWithEmptyName() throws Exception {
        // Arrange
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("");

        // Act & Assert
        mockMvc.perform(
                post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO))
        )
                .andExpect(status().isBadRequest());
    }
}
