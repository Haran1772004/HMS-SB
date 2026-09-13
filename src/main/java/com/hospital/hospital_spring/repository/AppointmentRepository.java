package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Integer> {
    boolean existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTimeAndStatus(
            int doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus status
    );

    boolean existsByPatientPatientIdAndAppointmentDateAndAppointmentTimeAndStatus(
            int patientId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus status
    );

    List<Appointment> findByPatientPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(
            int patientId
    );

    List<Appointment> findByDoctorDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(
            int doctorId
    );


    List<Appointment> findByAppointmentDateOrderByAppointmentTimeAsc(
            LocalDate appointmentDate
    );
    
    List<Appointment> findByStatusOrderByAppointmentDateAscAppointmentTimeAsc(
            AppointmentStatus status
    );
}