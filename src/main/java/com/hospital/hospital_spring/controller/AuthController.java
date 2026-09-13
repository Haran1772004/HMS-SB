package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.model.LoginRequest;
import com.hospital.hospital_spring.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getUsername(), request.getPassword());
        Map<String, String> response = new HashMap<>();
                            response.put("token", token);
                            response.put("message", "Login successful");

        return response;
    }
}