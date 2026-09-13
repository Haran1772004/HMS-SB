package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.exception.*;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    
    public void addPatient(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException("Patient is required");
        }

        ensureUniqueForAdd(patient);

        patientRepository.save(patient);
    }

     
    public void updatePatient(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException("Patient is required");
        }

        Patient existingPatient = patientRepository.findById(patient.takePatientId())
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patient.takePatientId()
                        )
                );

        ensureUniqueForUpdate(patient);

        existingPatient.setName(patient.takeName());
        existingPatient.setDob(patient.takeDob());
        existingPatient.setGender(patient.takeGender());
        existingPatient.setPhone(patient.takePhone());
        existingPatient.setEmail(patient.takeEmail());
        existingPatient.setStatus(patient.takeStatus());
        existingPatient.setUserId(patient.takeUserId());

        patientRepository.save(existingPatient);
    }

    
    public void removePatient(int patientId) {

        patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );

        patientRepository.deleteById(patientId);
    }

    
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


    public Patient takePatientById(int patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );
    }


    public List<Patient> takeAllPatients() {

        return patientRepository.findAll();
    }


    
    private void ensureUniqueForAdd(Patient patient) {

        if (patientRepository.existsByEmail(patient.takeEmail())) {
            throw new IllegalArgumentException(
                    "Patient with email already exists: " + patient.takeEmail()
            );
        }

        if (patientRepository.existsByPhone(patient.takePhone())) {
            throw new IllegalArgumentException(
                    "Patient with phone already exists: " + patient.takePhone()
            );
        }
    }


    
    private void ensureUniqueForUpdate(Patient patient) {

        if (patientRepository.existsByEmailAndPatientIdNot(
                patient.takeEmail(),
                patient.takePatientId())) {

            throw new IllegalArgumentException(
                    "Patient with email already exists: " + patient.takeEmail()
            );
        }

        if (patientRepository.existsByPhoneAndPatientIdNot(
                patient.takePhone(),
                patient.takePatientId())) {

            throw new IllegalArgumentException(
                    "Patient with phone already exists: " + patient.takePhone()
            );
        }
    }
}