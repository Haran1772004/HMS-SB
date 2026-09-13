package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.service.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;


    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    // Add doctor
    @PostMapping
    public void addDoctor(@RequestBody Doctor doctor) {

        doctorService.addDoctor(doctor);
    }


    // Update doctor
    @PutMapping("/{doctorId}")
    public void updateDoctor(
            @PathVariable int doctorId,
            @RequestBody Doctor doctor) {

        doctor.setDoctorId(doctorId);

        doctorService.updateDoctor(doctor);
    }


    // Deactivate doctor
    @PutMapping("/{doctorId}/deactivate")
    public void deactivateDoctor(
            @PathVariable int doctorId) {

        doctorService.deactivateDoctor(doctorId);
    }


    // Activate doctor
    @PutMapping("/{doctorId}/activate")
    public void activateDoctor(
            @PathVariable int doctorId) {

        doctorService.activateDoctor(doctorId);
    }


    // Get doctor by ID
    @GetMapping("/{doctorId}")
    public Doctor getDoctorById(
            @PathVariable int doctorId) {

        return doctorService.takeDoctorById(doctorId);
    }


    // Get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {

        return doctorService.takeAllDoctors();
    }
}