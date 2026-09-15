package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.service.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public void addDoctor(@RequestBody Doctor doctor) {

        doctorService.addDoctor(doctor);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{doctorId}")
    public void updateDoctor(
            @PathVariable int doctorId,
            @RequestBody Doctor doctor) {

        doctor.setDoctorId(doctorId);

        doctorService.updateDoctor(doctor);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{doctorId}/deactivate")
    public void deactivateDoctor(
            @PathVariable int doctorId) {

        doctorService.deactivateDoctor(doctorId);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{doctorId}/activate")
    public void activateDoctor(
            @PathVariable int doctorId) {

        doctorService.activateDoctor(doctorId);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{doctorId}")
    public Doctor getDoctorById(
            @PathVariable int doctorId) {

        return doctorService.takeDoctorById(doctorId);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @GetMapping
    public List<Doctor> getAllDoctors() {

        return doctorService.takeAllDoctors();
    }
}
