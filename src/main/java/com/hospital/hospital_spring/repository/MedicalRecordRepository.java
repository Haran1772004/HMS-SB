package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, Integer> {

    boolean existsByAppointmentAppointmentId(int appointmentId);

    Optional<MedicalRecord> findByAppointmentAppointmentId(
            int appointmentId
    );

    List<MedicalRecord> findByAppointmentPatientPatientIdOrderByRecordDateDescRecordIdDesc(
            int patientId
    );

    List<MedicalRecord> findByAppointmentDoctorDoctorIdOrderByRecordDateDescRecordIdDesc(
            int doctorId
    );

    List<MedicalRecord> findAllByOrderByRecordDateDescRecordIdDesc();
}