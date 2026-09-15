package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.entity.MedicalRecord;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.entity.Prescription;
import com.hospital.hospital_spring.exception.AuthorizationException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.model.PatientProfileResponse;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.security.CurrentUser;
import com.hospital.hospital_spring.service.AppointmentService;
import com.hospital.hospital_spring.service.MedicalRecordService;
import com.hospital.hospital_spring.service.PatientAddressService;
import com.hospital.hospital_spring.service.PatientService;
import com.hospital.hospital_spring.service.PrescriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final PrescriptionService prescriptionService;
    private final PatientAddressService patientAddressService;

    public PatientController(
            PatientService patientService,
            PatientRepository patientRepository,
            AppointmentService appointmentService,
            MedicalRecordService medicalRecordService,
            PrescriptionService prescriptionService,
            PatientAddressService patientAddressService) {

        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.prescriptionService = prescriptionService;
        this.patientAddressService = patientAddressService;
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PostMapping
    public void addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/{patientId}")
    public void updatePatient(
            @PathVariable int patientId,
            @RequestBody Patient patient) {

        patient.setPatientId(patientId);
        patientService.updatePatient(patient);
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{patientId}")
    public void removePatient(@PathVariable int patientId) {
        patientService.removePatient(patientId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/{patientId}/deactivate")
    public void deactivatePatient(@PathVariable int patientId) {
        patientService.deactivatePatient(patientId);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/{patientId}/activate")
    public void activatePatient(@PathVariable int patientId) {
        patientService.activatePatient(patientId);
    }

 
     
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public Patient getMyPersonalDetails() {
        return getAuthenticatedPatient();
    }

   
    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/me")
    public void updateMyPersonalDetails(@RequestBody Patient patient) {
        Patient ownPatient = getAuthenticatedPatient();
        patient.setPatientId(ownPatient.takePatientId());
        patient.setStatus(ownPatient.takeStatus());
        patient.setUserId(ownPatient.takeUserId());
        patientService.updatePatient(patient);
    }

   
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me/profile")
    public PatientProfileResponse getMyFullProfile() {
        Patient ownPatient = getAuthenticatedPatient();
        List<PatientAddress> addresses =
                patientAddressService.takeAddressesByPatient(ownPatient.takePatientId());
        return new PatientProfileResponse(ownPatient, addresses);
    }

   
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me/appointments")
    public List<Appointment> getMyAppointments() {
        Patient ownPatient = getAuthenticatedPatient();
        return appointmentService.takeAppointmentsByPatient(ownPatient.takePatientId());
    }

    
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me/medical-records")
    public List<MedicalRecord> getMyMedicalRecords() {
        Patient ownPatient = getAuthenticatedPatient();
        return medicalRecordService.takeRecordsByPatient(ownPatient.takePatientId());
    }

    
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me/prescriptions")
    public List<Prescription> getMyPrescriptions() {
        Patient ownPatient = getAuthenticatedPatient();
        return prescriptionService.takePrescriptionsByPatient(ownPatient.takePatientId());
    }


     
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @GetMapping("/{patientId}")
    public Patient getPatientById(@PathVariable int patientId) {

        if (isPatientRole()) {
            ensureOwnPatient(patientId);
        }

        return patientService.takePatientById(patientId);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.takeAllPatients();
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping("/status/{status}")
    public List<Patient> getPatientsByStatus(
            @PathVariable String status) {

        AccountStatus accountStatus;

        try {
            accountStatus = AccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid status. Accepted: PENDING, ACTIVE, INACTIVE, REJECTED"
            );
        }

        return patientService.takePatientsByStatus(accountStatus);
    }

    private boolean isPatientRole() {
        return "ROLE_PATIENT".equals(CurrentUser.getRole());
    }

    private Patient getAuthenticatedPatient() {
        int currentUserId = CurrentUser.getUserId();
        return patientRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No patient profile linked to your account."
                        )
                );
    }

    private void ensureOwnPatient(int requestedPatientId) {
        Patient ownPatient = getAuthenticatedPatient();
        if (ownPatient.takePatientId() != requestedPatientId) {
            throw new AuthorizationException(
                    "Access denied: you can only view your own patient profile."
            );
        }
    }
}
