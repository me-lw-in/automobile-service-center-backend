package com.example.serviceassistantbackend.service.user;

import com.example.serviceassistantbackend.dto.user.UserDTO;
import com.example.serviceassistantbackend.entity.User;
import com.example.serviceassistantbackend.repository.RoleRepository;
import com.example.serviceassistantbackend.repository.UserRepository;
import com.example.serviceassistantbackend.util.StringCapitalizeUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void createUser(UserDTO dto){
        var email = dto.getEmail().trim().toLowerCase();
        var userRole = dto.getRoleName().toUpperCase();
        var phone = dto.getPhone().trim();
        if (userRepository.findByEmail(email).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with " + email + " already exists!"
            );
        }
        var role = roleRepository.findByName(userRole.toUpperCase()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        userRole + " role not found"
                )
        );
        String name = StringCapitalizeUtil.capitalizeEachWord(dto.getName().trim());
        String password = passwordEncoder.encode(dto.getPassword());
        var user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);

    }

}
