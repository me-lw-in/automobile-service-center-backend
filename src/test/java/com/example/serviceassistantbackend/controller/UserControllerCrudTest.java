package com.example.serviceassistantbackend.controller;

import com.example.serviceassistantbackend.dto.user.UserDTO;
import com.example.serviceassistantbackend.entity.Role;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
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
@DisplayName("User CRUD Operations Test Suite")
public class UserControllerCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Role testRole;

    @BeforeEach
    void setUp() {


        testRole = new Role();
        testRole.setName("Mechanic_" + System.currentTimeMillis());
        testRole = roleRepository.save(testRole);
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("CREATE - Successfully create a new user")
    void testCreateUser() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Alice Smith");
        userDTO.setPhone("9876543210");
        userDTO.setEmail("alice@example.com");
        userDTO.setPassword("SecurePass123");
        userDTO.setRoleName(testRole.getName());

        // Act & Assert
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo("Alice Smith")))
                .andExpect(jsonPath("$.phone", equalTo("9876543210")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve all users")
    void testGetAllUsers() throws Exception {
        // Arrange
        User user1 = new User();
        user1.setName("User One");
        user1.setPhone("1234567890");
        user1.setEmail("user1@example.com");
        user1.setPassword("pass");
        user1.setRole(testRole);

        User user2 = new User();
        user2.setName("User Two");
        user2.setPhone("0987654321");
        user2.setEmail("user2@example.com");
        user2.setPassword("pass");
        user2.setRole(testRole);

        userRepository.save(user1);
        userRepository.save(user2);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].name", hasItems("User One", "User Two")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("READ - Successfully retrieve user by ID")
    void testGetUserById() throws Exception {
        // Arrange
        User user = new User();
        user.setName("Bob Johnson");
        user.setPhone("5555555555");
        user.setEmail("bob@example.com");
        user.setPassword("pass");
        user.setRole(testRole);
        User savedUser = userRepository.save(user);

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Bob Johnson")))
                .andExpect(jsonPath("$.email", equalTo("bob@example.com")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("UPDATE - Successfully update an existing user")
    void testUpdateUser() throws Exception {
        // Arrange
        User user = new User();
        user.setName("Charlie Brown");
        user.setPhone("4444444444");
        user.setEmail("charlie@example.com");
        user.setPassword("pass");
        user.setRole(testRole);
        User savedUser = userRepository.save(user);

        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("Charlie Updated");
        updateDTO.setPhone("3333333333");
        updateDTO.setEmail("charlie.updated@example.com");
        updateDTO.setPassword("NewSecurePass123");
        updateDTO.setRoleName(testRole.getName());

        // Act & Assert
        mockMvc.perform(
                put("/api/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Charlie Updated")))
                .andExpect(jsonPath("$.phone", equalTo("3333333333")));
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("DELETE - Successfully delete a user")
    void testDeleteUser() throws Exception {
        // Arrange
        User user = new User();
        user.setName("David Lee");
        user.setPhone("6666666666");
        user.setEmail("david@example.com");
        user.setPassword("pass");
        user.setRole(testRole);
        User savedUser = userRepository.save(user);

        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNotFound());
    }

        @Test
        @WithMockUser(roles = "SUPER_ADMIN")
    @DisplayName("CREATE - User validation - empty email should fail")
    void testCreateUserWithEmptyEmail() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Invalid User");
        userDTO.setPhone("9999999999");
        userDTO.setEmail("");
        userDTO.setPassword("pass");
        userDTO.setRoleName(testRole.getName());

        // Act & Assert
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
        )
                .andExpect(status().isBadRequest());
    }
}
