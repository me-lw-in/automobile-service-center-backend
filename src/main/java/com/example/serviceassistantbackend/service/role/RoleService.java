package com.example.serviceassistantbackend.service.role;

import com.example.serviceassistantbackend.dto.role.RoleDTO;
import com.example.serviceassistantbackend.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(RoleDTO dto);
    Role updateRole(Long id, RoleDTO dto);
    void deleteRole(Long id);
}
