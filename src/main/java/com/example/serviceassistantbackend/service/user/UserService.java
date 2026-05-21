package com.example.serviceassistantbackend.service.user;

import com.example.serviceassistantbackend.dto.user.UserDTO;
import com.example.serviceassistantbackend.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserDTO dto);
    List<UserResponseDTO> getUsers(String roleName, String search);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO updateUser(Long id, UserDTO dto);
    void deleteUser(Long id);


}
