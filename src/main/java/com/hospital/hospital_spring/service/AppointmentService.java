package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.exception.AppointmentNotFoundException;
import com.hospital.hospital_spring.exception.DoctorNotFoundException;
import com.hospital.hospital_spring.exception.PatientNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.model.AppointmentStatus;
import com.hospital.hospital_spring.repository.AppointmentRepository;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.*;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;


    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public void bookAppointment(Appointment appointment) {

        if (appointment == null ||
                appointment.takePatient() == null ||
                appointment.takeDoctor() == null) {

            throw new IllegalArgumentException(
                    "Patient and doctor are required"
            );
        }


        int patientId =
                appointment.takePatient().takePatientId();

        Patient patient =
               patientRepository.findById(patientId)
              .orElseThrow(() ->
               new PatientNotFoundException(
                                        "Patient not found with ID: "
                                                + patientId
                                )
                        );

        int doctorId =
                appointment.takeDoctor().takeDoctorId();

        Doctor doctor =
                doctorRepository.findById(doctorId)
                        .orElseThrow(() ->
                                new DoctorNotFoundException(
                                        "Doctor not found with ID: "
                                                + doctorId
                                )
                        );

        if (patient.takeStatus() != AccountStatus.ACTIVE ||
                doctor.takeStatus() != AccountStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Patient and doctor must be active"
            );
        }

        LocalDate date =
                appointment.takeAppointmentDate();

        if (date == null) {

            throw new IllegalArgumentException(
                    "Invalid appointment date or time"
            );
        }

        LocalTime time =
                appointment.takeAppointmentTime();

        if (time == null) {

            throw new IllegalArgumentException(
                    "Invalid appointment date or time"
            );
        }

        if (LocalDateTime.of(date, time)
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Appointment cannot be in the past"
            );
        }

        time = time.withNano(0);

        if (!isDoctorAvailable(
                doctorId,
                date,
                time)) {

            throw new IllegalArgumentException(
                    "Doctor or patient already has an appointment at that time"
            );
        }

        if (hasPatientConflict(
                patientId,
                date,
                time)) {

            throw new IllegalArgumentException(
                    "Doctor or patient already has an appointment at that time"
            );
        }



        appointment.setPatient(patient);
        appointment.setDoctor(doctor);


        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);

        appointment.setStatus(
                AppointmentStatus.SCHEDULED
        );

        appointmentRepository.save(appointment);
    }


    public void cancelAppointment(int appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found with ID: "
                                                + appointmentId
                                )
                        );

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        appointmentRepository.save(appointment);
    }

    public boolean isDoctorAvailable(
            int doctorId,
            LocalDate date,
            LocalTime time) {

        return !appointmentRepository
                .existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTimeAndStatus(
                        doctorId,
                        date,
                        time,
                        AppointmentStatus.SCHEDULED
                );
    }
    
    public boolean isDoctorAvailable(
            int doctorId,
            String date,
            String time) {

        LocalDate parsedDate =
                parseDate(date);

        LocalTime parsedTime =
                parseTime(time);

        if (parsedDate == null ||
                parsedTime == null) {

            return false;
        }

        return isDoctorAvailable(
                doctorId,
                parsedDate,
                parsedTime
        );
    }

    private boolean hasPatientConflict(
            int patientId,
            LocalDate date,
            LocalTime time) {

        return appointmentRepository
                .existsByPatientPatientIdAndAppointmentDateAndAppointmentTimeAndStatus(
                        patientId,
                        date,
                        time,
                        AppointmentStatus.SCHEDULED
                );
    }

    public List<Appointment> takeAppointmentsByPatient(
            int patientId) {

        return appointmentRepository
                .findByPatientPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        patientId
                );
    }
    public List<Appointment> takeAppointmentsByDoctor(
            int doctorId) {

        return appointmentRepository
                .findByDoctorDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        doctorId
                );
    }
    public List<Appointment> takeTodaysAppointments() {

        return appointmentRepository
                .findByAppointmentDateOrderByAppointmentTimeAsc(
                        LocalDate.now()
                );
    }
    public List<Appointment> takeAllAppointments() {

        return appointmentRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Appointment::takeAppointmentDate)
                .thenComparing(Appointment::takeAppointmentTime))
                .collect(java.util.stream.Collectors.toList());

    }

    private LocalDate parseDate(String date) {

        if (date == null) {
            return null;
        }

        try {
            return LocalDate.parse(date);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    private LocalTime parseTime(String time) {

        if (time == null) {
            return null;
        }

        try {
            return LocalTime.parse(time);

        } catch (RuntimeException exception) {

            try {
                java.time.format.DateTimeFormatter formatter =
                        java.time.format.DateTimeFormatter
                                .ofPattern("h:mm a");

                return LocalTime.parse(
                        time.toUpperCase(),
                        formatter
                );

            } catch (RuntimeException ignored) {
                throw new IllegalArgumentException("Time is not correct format");
            }
        }
    }
}