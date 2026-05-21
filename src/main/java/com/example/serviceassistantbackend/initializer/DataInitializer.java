package com.example.serviceassistantbackend.initializer;

import com.example.serviceassistantbackend.entity.Role;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${app.super-admin.email}")
    private String SUPER_ADMIN_EMAIL;
    @Value("${app.super-admin.password}")
    private String SUPER_ADMIN_PASSWORD;
    @Value("${app.super-admin.name}")
    private String SUPER_ADMIN_NAME;
    @Value("${app.super-admin.phone}")
    private String SUPER_ADMIN_PHONE;


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        List.of("SUPER_ADMIN", "SERVICE_INCHARGE", "SERVICE_MANAGER", "MECHANIC", "CUSTOMER")
                .forEach(roleName -> roleRepository
                        .findByName(roleName)
                        .orElseGet(() -> {
                            var role = new Role();
                            role.setName(roleName);
                            return roleRepository.save(role);
                        }));

        var superAdminRole = roleRepository
                .findByName("SUPER_ADMIN")
                .orElseThrow();

        boolean userExists = userRepository.existsByEmail(SUPER_ADMIN_EMAIL);

        if (!userExists) {
            var user = new User();
            user.setName(SUPER_ADMIN_NAME);
            user.setEmail(SUPER_ADMIN_EMAIL);
            user.setPassword(passwordEncoder.encode(SUPER_ADMIN_PASSWORD));
            user.setRole(superAdminRole);
            user.setPhone(SUPER_ADMIN_PHONE);
            userRepository.save(user);
        }
    }
}
