package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndPatientIdNot(String email, int patientId);

    boolean existsByPhoneAndPatientIdNot(String phone, int patientId);

    /**
     * Used by ownership checks:
     * find the Patient linked to a given User account.
     */
    Optional<Patient> findByUserId(int userId);

    /**
     * Used by the receptionist/admin to filter patients by account status.
     * e.g. findByStatus(AccountStatus.PENDING)  -> pending patients
     *      findByStatus(AccountStatus.INACTIVE) -> deactivated patients
     */
    List<Patient> findByStatus(AccountStatus status);
}
