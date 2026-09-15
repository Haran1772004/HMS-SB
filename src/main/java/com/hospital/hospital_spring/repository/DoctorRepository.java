package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndDoctorIdNot(String email, int doctorId);

    boolean existsByPhoneAndDoctorIdNot(String phone, int doctorId);

    /**
     * Used by ownership checks:
     * find the Doctor linked to a given User account.
     */
    Optional<Doctor> findByUserId(int userId);

    /**
     * Used by AdminService to list doctors by account status.
     * e.g. findByStatus(AccountStatus.PENDING) -> pending approval queue
     */
    List<Doctor> findByStatus(AccountStatus status);
}
