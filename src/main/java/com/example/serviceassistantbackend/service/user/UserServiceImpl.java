package com.example.serviceassistantbackend.service.user;

import com.example.serviceassistantbackend.dto.user.UserDTO;
import com.example.serviceassistantbackend.dto.user.UserResponseDTO;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import com.example.serviceassistantbackend.repository.VehicleRepository;
import com.example.serviceassistantbackend.repository.JobCardRepository;
import com.example.serviceassistantbackend.repository.JobServiceRepository;
import com.example.serviceassistantbackend.util.StringCapitalizeUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VehicleRepository vehicleRepository;
    private final JobCardRepository jobCardRepository;
    private final JobServiceRepository jobServiceRepository;

    @Override
    public UserResponseDTO createUser(UserDTO dto) {
        var email = dto.getEmail().trim().toLowerCase();
        var userRole = dto.getRoleName().trim().toUpperCase();
        var phone = dto.getPhone().trim();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with " + email + " already exists!"
            );
        }

        var role = roleRepository.findByName(userRole)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        userRole + " role not found"
                ));

        String name = StringCapitalizeUtil.capitalizeEachWord(dto.getName().trim());
        String password = passwordEncoder.encode(dto.getPassword());

        var user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);

        return mapToDto(user);
    }

    @Override
    public List<UserResponseDTO> getUsers(String roleName, String search) {
        var users = userRepository.findAll();

        if (roleName != null && !roleName.isBlank()) {
            var normalizedRole = roleName.trim().toUpperCase();
            users = users.stream()
                    .filter(user -> user.getRole() != null && normalizedRole.equals(user.getRole().getName()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.isBlank()) {
            Pattern pattern;
            try {
                pattern = Pattern.compile(search.trim(), Pattern.CASE_INSENSITIVE);
            } catch (PatternSyntaxException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search pattern");
            }

            users = users.stream()
                    .filter(user -> pattern.matcher(user.getName()).find())
                    .collect(Collectors.toList());
        }

        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        return mapToDto(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        var user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        return mapToDto(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserDTO dto) {
        var existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        var userRole = dto.getRoleName().trim().toUpperCase();
        var role = roleRepository.findByName(userRole)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        userRole + " role not found"
                ));

        existing.setName(StringCapitalizeUtil.capitalizeEachWord(dto.getName().trim()));
        existing.setPhone(dto.getPhone().trim());
        existing.setEmail(dto.getEmail().trim().toLowerCase());
        existing.setRole(role);
        existing.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(existing);
        return mapToDto(existing);
    }

    @Override
    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        boolean hasVehicles = vehicleRepository.existsByOwnerId(id);
        boolean hasJobCardsCreated = jobCardRepository.existsByCreatedById(id);
        boolean hasJobCardsAssigned = jobCardRepository.existsByAssignedMechanicId(id);
        boolean hasJobServices = jobServiceRepository.existsByPerformedById(id);

        var reasons = new java.util.ArrayList<String>();
        if (hasVehicles) reasons.add("owns vehicles");
        if (hasJobCardsCreated) reasons.add("created job cards");
        if (hasJobCardsAssigned) reasons.add("is assigned to job cards");
        if (hasJobServices) reasons.add("performed job services");

        if (!reasons.isEmpty()) {
            String message = "Cannot delete user: user " + String.join(", ", reasons) + ".";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        userRepository.delete(user);
    }

    private UserResponseDTO mapToDto(User user) {
        var dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRoleName(user.getRole() != null ? user.getRole().getName() : null);
        return dto;
    }
}
