package com.example.serviceassistantbackend.service.role;

import com.example.serviceassistantbackend.dto.role.RoleDTO;
import com.example.serviceassistantbackend.entity.Role;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found."));
    }

    @Override
    public Role createRole(RoleDTO dto) {
        String roleName = dto.getName().trim().toUpperCase();
        if (roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name is required.");
        }
        roleRepository.findByName(roleName).ifPresent(role -> {
            throw new IllegalArgumentException("Role already exists.");
        });

        var role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, RoleDTO dto) {
        String roleName = dto.getName().trim().toUpperCase();
        if (roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name is required.");
        }
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found."));

        roleRepository.findByName(roleName)
                .filter(role -> !role.getId().equals(id))
                .ifPresent(role -> {
                    throw new IllegalArgumentException("Role name already exists.");
                });

        existing.setName(roleName);
        return roleRepository.save(existing);
    }

    @Override
    public void deleteRole(Long id) {
        var role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found."));
        if (userRepository.existsByRoleId(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Cannot delete role: users are assigned to this role"
            );
        }
        roleRepository.delete(role);
    }
}
