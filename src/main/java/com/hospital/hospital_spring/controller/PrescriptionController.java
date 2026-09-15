package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.MedicalRecord;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.entity.Prescription;
import com.hospital.hospital_spring.exception.AuthorizationException;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.MedicalRecordRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.security.CurrentUser;
import com.hospital.hospital_spring.service.PrescriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionController(
            PrescriptionService prescriptionService,
            MedicalRecordRepository medicalRecordRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.prescriptionService = prescriptionService;
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }


     
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public void addPrescription(
            @RequestBody Prescription prescription) {

        ensureDoctorOwnsMedicalRecord(prescription);

        prescriptionService.addPrescription(prescription);
    }

 
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @GetMapping("/record/{recordId}")
    public List<Prescription> getPrescriptionsByRecord(
            @PathVariable int recordId) {

        if (isPatientRole()) {
            ensureRecordBelongsToOwnPatient(recordId);
        }

        return prescriptionService.takePrescriptionsByRecord(recordId);
    }


    private boolean isPatientRole() {
        return "ROLE_PATIENT".equals(CurrentUser.getRole());
    }

    private void ensureDoctorOwnsMedicalRecord(Prescription prescription) {

        if (prescription == null || prescription.takeRecordId() <= 0) {
            return;
        }

        MedicalRecord record = medicalRecordRepository
                .findById(prescription.takeRecordId())
                .orElseThrow(() ->
                        new AuthorizationException(
                                "Medical record not found or access denied."
                        )
                );

        int recordDoctorId = record.takeAppointment()
                .takeDoctor()
                .takeDoctorId();

        int currentUserId = CurrentUser.getUserId();

        Doctor ownDoctor = doctorRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No doctor profile linked to your account."
                        )
                );

        if (ownDoctor.takeDoctorId() != recordDoctorId) {

            throw new AuthorizationException(
                    "Access denied: you can only prescribe for your own medical records."
            );
        }
    }

    private void ensureRecordBelongsToOwnPatient(int recordId) {

        MedicalRecord record = medicalRecordRepository
                .findById(recordId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "Record not found or access denied."
                        )
                );

        int recordPatientId = record.takeAppointment()
                .takePatient()
                .takePatientId();

        int currentUserId = CurrentUser.getUserId();

        Patient ownPatient = patientRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No patient profile linked to your account."
                        )
                );

        if (ownPatient.takePatientId() != recordPatientId) {

            throw new AuthorizationException(
                    "Access denied: you can only view your own prescriptions."
            );
        }
    }
}
