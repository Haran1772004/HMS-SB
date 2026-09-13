package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    
    @PostMapping
    public void addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
    }

    
    @PutMapping("/{patientId}")
    public void updatePatient(
            @PathVariable int patientId,
            @RequestBody Patient patient) {

        patient.setPatientId(patientId);

        patientService.updatePatient(patient);
    }

    
    @DeleteMapping("/{patientId}")
    public void removePatient(@PathVariable int patientId) {

        patientService.removePatient(patientId);
    }

    
    @PutMapping("/{patientId}/deactivate")
    public void deactivatePatient(@PathVariable int patientId) {

        patientService.deactivatePatient(patientId);
    }

    
    @PutMapping("/{patientId}/activate")
    public void activatePatient(@PathVariable int patientId) {

        patientService.activatePatient(patientId);
    }

    @GetMapping("/{patientId}")
    public Patient getPatientById(@PathVariable int patientId) {

        return patientService.takePatientById(patientId);
    }

    
    @GetMapping
    public List<Patient> getAllPatients() {

        return patientService.takeAllPatients();
    }
}