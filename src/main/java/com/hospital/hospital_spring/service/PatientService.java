package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.exception.ConflictException;
import com.hospital.hospital_spring.exception.PatientNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.PatientAddressRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientAddressRepository patientAddressRepository;

    public PatientService(
            PatientRepository patientRepository,
            PatientAddressRepository patientAddressRepository) {

        this.patientRepository = patientRepository;
        this.patientAddressRepository = patientAddressRepository;
    }

    // --- Add patient ---

    @Transactional
    public void addPatient(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException("Patient is required");
        }

        validatePatientFields(patient);
        ensureUniqueForAdd(patient);

        if (patient.takeStatus() == null) {
            patient.setStatus(AccountStatus.PENDING);
        }

        patientRepository.save(patient);
    }

    // --- Update patient ---

    @Transactional
    public void updatePatient(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException("Patient is required");
        }

        Patient existingPatient = patientRepository
                .findById(patient.takePatientId())
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patient.takePatientId()
                        )
                );

        validatePatientFields(patient);
        ensureUniqueForUpdate(patient);

        existingPatient.setName(patient.takeName());
        existingPatient.setDob(patient.takeDob());
        existingPatient.setGender(patient.takeGender());
        existingPatient.setPhone(patient.takePhone());
        existingPatient.setEmail(patient.takeEmail());

        patientRepository.save(existingPatient);
    }

    // --- Remove patient (cascades address deletion atomically) ---

    @Transactional
    public void removePatient(int patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(
                    "Patient not found with ID: " + patientId
            );
        }

        patientAddressRepository.deleteByPatientId(patientId);
        patientRepository.deleteById(patientId);
    }

    // --- Deactivate patient ---

    @Transactional
    public void deactivatePatient(int patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );

        patient.setStatus(AccountStatus.INACTIVE);
        patientRepository.save(patient);
    }

    // --- Activate patient ---

    @Transactional
    public void activatePatient(int patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );

        patient.setStatus(AccountStatus.ACTIVE);
        patientRepository.save(patient);
    }

    // --- Get patient by ID ---

    public Patient takePatientById(int patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );
    }

    // --- Get patient by User ID ---

    public Patient takePatientByUserId(int userId) {

        return patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "No patient profile linked to user ID: " + userId
                        )
                );
    }

    // --- Get all patients ---

    public List<Patient> takeAllPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> takePatientsByStatus(AccountStatus status) {
        return patientRepository.findByStatus(status);
    }

    // --- Validation helpers ---

    private void validatePatientFields(Patient patient) {

        if (patient.takeName() == null || patient.takeName().isBlank()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        if (patient.takeName().trim().length() < 2 || patient.takeName().trim().length() > 60) {
            throw new IllegalArgumentException("Patient name must be 2-60 characters");
        }

        if (patient.takeDob() != null && patient.takeDob().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        if (patient.takePhone() != null && !patient.takePhone().isBlank()) {
            if (!patient.takePhone().matches("^\\+?[0-9]{7,15}$")) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }

        if (patient.takeEmail() != null && !patient.takeEmail().isBlank()) {
            if (!patient.takeEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    // --- Uniqueness checks ---

    private void ensureUniqueForAdd(Patient patient) {

        if (patient.takeEmail() != null && !patient.takeEmail().isBlank()) {
            if (patientRepository.existsByEmail(patient.takeEmail())) {
                throw new ConflictException(
                        "Patient with email already exists: " + patient.takeEmail()
                );
            }
        }

        if (patient.takePhone() != null && !patient.takePhone().isBlank()) {
            if (patientRepository.existsByPhone(patient.takePhone())) {
                throw new ConflictException(
                        "Patient with phone already exists: " + patient.takePhone()
                );
            }
        }
    }

    private void ensureUniqueForUpdate(Patient patient) {

        if (patient.takeEmail() != null && !patient.takeEmail().isBlank()) {
            if (patientRepository.existsByEmailAndPatientIdNot(
                    patient.takeEmail(), patient.takePatientId())) {
                throw new ConflictException(
                        "Patient with email already exists: " + patient.takeEmail()
                );
            }
        }

        if (patient.takePhone() != null && !patient.takePhone().isBlank()) {
            if (patientRepository.existsByPhoneAndPatientIdNot(
                    patient.takePhone(), patient.takePatientId())) {
                throw new ConflictException(
                        "Patient with phone already exists: " + patient.takePhone()
                );
            }
        }
    }
}
