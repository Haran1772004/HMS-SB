package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndPatientIdNot(String email, int patientId);

    boolean existsByPhoneAndPatientIdNot(String phone, int patientId);
}