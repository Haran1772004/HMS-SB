package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.MedicalRecord;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.exception.AuthorizationException;
import com.hospital.hospital_spring.repository.AppointmentRepository;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.MedicalRecordRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.security.CurrentUser;
import com.hospital.hospital_spring.service.MedicalRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public MedicalRecordController(
            MedicalRecordService medicalRecordService,
            MedicalRecordRepository medicalRecordRepository,
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.medicalRecordService = medicalRecordService;
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

   
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public void createMedicalRecord(@RequestBody MedicalRecord record) {

        ensureDoctorOwnsAppointment(record);
        medicalRecordService.createMedicalRecord(record);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{recordId}")
    public MedicalRecord getRecordById(@PathVariable int recordId) {

        if (isPatientRole()) {
            ensureRecordBelongsToOwnPatient(recordId);
        }

        return medicalRecordService.takeRecordById(recordId);
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/appointment/{appointmentId}")
    public MedicalRecord getRecordByAppointment(
            @PathVariable int appointmentId) {

        if (isDoctorRole()) {
            ensureDoctorOwnsAppointmentById(appointmentId);
        }

        return medicalRecordService.takeRecordByAppointment(appointmentId);
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getRecordsByPatient(
            @PathVariable int patientId) {

        if (isPatientRole()) {
            ensureOwnPatient(patientId);
        }

        return medicalRecordService.takeRecordsByPatient(patientId);
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public List<MedicalRecord> getRecordsByDoctor(
            @PathVariable int doctorId) {

        if (isDoctorRole()) {
            ensureOwnDoctor(doctorId);
        }

        return medicalRecordService.takeRecordsByDoctor(doctorId);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping
    public List<MedicalRecord> getAllRecords() {
        return medicalRecordService.takeAllRecords();
    }

  

    private boolean isPatientRole() {
        return "ROLE_PATIENT".equals(CurrentUser.getRole());
    }

    private boolean isDoctorRole() {
        return "ROLE_DOCTOR".equals(CurrentUser.getRole());
    }

    private void ensureDoctorOwnsAppointment(MedicalRecord record) {

        if (record == null || record.takeAppointment() == null) return;

        int appointmentId = record.takeAppointment().takeAppointmentId();
        ensureDoctorOwnsAppointmentById(appointmentId);
    }

    private void ensureDoctorOwnsAppointmentById(int appointmentId) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "Appointment not found or access denied."
                        )
                );

        int appointmentDoctorId = appointment.takeDoctor().takeDoctorId();

        int currentUserId = CurrentUser.getUserId();

        Doctor ownDoctor = doctorRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No doctor profile linked to your account."
                        )
                );

        if (ownDoctor.takeDoctorId() != appointmentDoctorId) {
            throw new AuthorizationException(
                    "Access denied: you can only access records for your own appointments."
            );
        }
    }

    private void ensureOwnDoctor(int requestedDoctorId) {

        int currentUserId = CurrentUser.getUserId();

        Doctor ownDoctor = doctorRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No doctor profile linked to your account."
                        )
                );

        if (ownDoctor.takeDoctorId() != requestedDoctorId) {
            throw new AuthorizationException(
                    "Access denied: you can only view your own medical records."
            );
        }
    }

    private void ensureOwnPatient(int requestedPatientId) {

        int currentUserId = CurrentUser.getUserId();

        Patient ownPatient = patientRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No patient profile linked to your account."
                        )
                );

        if (ownPatient.takePatientId() != requestedPatientId) {
            throw new AuthorizationException(
                    "Access denied: you can only view your own medical records."
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

        ensureOwnPatient(recordPatientId);
    }
}
