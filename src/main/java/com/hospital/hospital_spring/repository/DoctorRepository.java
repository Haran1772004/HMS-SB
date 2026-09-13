package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndDoctorIdNot(String email, int doctorId);

    boolean existsByPhoneAndDoctorIdNot(String phone, int doctorId);
}