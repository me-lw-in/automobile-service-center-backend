package com.example.serviceassistantbackend.controller;


import com.example.serviceassistantbackend.dto.auth.LoginDTO;
import com.example.serviceassistantbackend.dto.user.UserDTO;
import com.example.serviceassistantbackend.service.jwt.JwtService;
import com.example.serviceassistantbackend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Void> createAccount(@RequestBody @Valid UserDTO dto){
        userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody @Valid LoginDTO dto){
        String email = dto.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email,
                dto.getPassword()
        ));
        var token = jwtService.generateToken(email);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("accessToken", token));
    }
}
