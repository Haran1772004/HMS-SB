package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/doctors/{username}/approve")
    public ResponseEntity<Map<String, String>> approveDoctor(@PathVariable String username) {

        adminService.approveDoctor(username);

        Map<String, String> response = new HashMap<>();
        response.put("message",
                "Doctor '" + username + "' has been approved. They can now log in.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/doctors/{username}/reject")
    public ResponseEntity<Map<String, String>> rejectDoctor(
            @PathVariable String username) {

        adminService.rejectDoctor(username);

        Map<String, String> response = new HashMap<>();
        response.put("message",
                "Doctor '" + username + "' registration has been rejected.");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<Map<String, String>> removeUserAndProfile(@PathVariable String username) {

        adminService.removeUserAndProfile(username);

        Map<String, String> response = new HashMap<>();
        response.put("message",
                "User '" + username + "' and associated profile have been removed.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctors/pending")
    public List<Doctor> getPendingDoctors() {

        return adminService.takePendingDoctors();
    }
}
