package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.model.DoctorRegistrationRequest;
import com.hospital.hospital_spring.model.LoginRequest;
import com.hospital.hospital_spring.model.LoginResponse;
import com.hospital.hospital_spring.model.PatientRegistrationRequest;
import com.hospital.hospital_spring.service.AuthService;
import com.hospital.hospital_spring.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RegistrationService registrationService;

    public AuthController(AuthService authService,RegistrationService registrationService) {

        this.authService = authService;
        this.registrationService = registrationService;
    }
 
   @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getUsername(),request.getPassword());

        return ResponseEntity.ok(new LoginResponse(token, "Login successful"));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<Map<String, String>> registerPatient(
        @RequestBody PatientRegistrationRequest request) {

        registrationService.registerPatient(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Patient registered successfully. You can now log in.");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Map<String, String>> registerDoctor(
            @RequestBody DoctorRegistrationRequest request) {

        registrationService.registerDoctor(request);

        Map<String, String> response = new HashMap<>();
        response.put("message",
                "Doctor registration submitted successfully. "
                + "Your account is pending admin approval.");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
