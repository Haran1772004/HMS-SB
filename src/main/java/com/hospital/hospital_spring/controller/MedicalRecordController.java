package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.MedicalRecord;
import com.hospital.hospital_spring.service.MedicalRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;


    public MedicalRecordController(MedicalRecordService medicalRecordService) {

        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public void createMedicalRecord(@RequestBody MedicalRecord record) {

        medicalRecordService.createMedicalRecord(record);
    }

    @GetMapping("/{recordId}")
    public MedicalRecord getRecordById(
            @PathVariable int recordId) {

        return medicalRecordService
                .takeRecordById(recordId);
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getRecordsByPatient(
            @PathVariable int patientId) {

        return medicalRecordService
                .takeRecordsByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<MedicalRecord> getRecordsByDoctor(
            @PathVariable int doctorId) {

        return medicalRecordService
                .takeRecordsByDoctor(doctorId);
    }

    @GetMapping
    public List<MedicalRecord> getAllRecords() {

        return medicalRecordService
                .takeAllRecords();
    }
}