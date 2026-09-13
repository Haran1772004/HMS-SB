package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Prescription;
import com.hospital.hospital_spring.service.PrescriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public void addPrescription(
            @RequestBody Prescription prescription) {

        prescriptionService.addPrescription(prescription);
    }

    @GetMapping("/record/{recordId}")
    public List<Prescription> getPrescriptionsByRecord(
            @PathVariable int recordId) {

        return prescriptionService
                .takePrescriptionsByRecord(recordId);
    }
}