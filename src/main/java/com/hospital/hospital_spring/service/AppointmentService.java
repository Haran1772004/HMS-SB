package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.exception.AppointmentNotFoundException;
import com.hospital.hospital_spring.exception.ConflictException;
import com.hospital.hospital_spring.exception.DoctorNotFoundException;
import com.hospital.hospital_spring.exception.PatientNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.model.AppointmentStatus;
import com.hospital.hospital_spring.repository.AppointmentRepository;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    // --- Book appointment ---

    @Transactional
    public void bookAppointment(Appointment appointment) {

        if (appointment == null ||
                appointment.takePatient() == null ||
                appointment.takeDoctor() == null) {

            throw new IllegalArgumentException(
                    "Patient and doctor are required"
            );
        }

        int patientId = appointment.takePatient().takePatientId();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: " + patientId
                        )
                );

        int doctorId = appointment.takeDoctor().takeDoctorId();

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with ID: " + doctorId
                        )
                );

        if (patient.takeStatus() != AccountStatus.ACTIVE ||
                doctor.takeStatus() != AccountStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Patient and doctor must be active"
            );
        }

        LocalDate date = appointment.takeAppointmentDate();

        if (date == null) {
            throw new IllegalArgumentException("Invalid appointment date or time");
        }

        LocalTime time = appointment.takeAppointmentTime();

        if (time == null) {
            throw new IllegalArgumentException("Invalid appointment date or time");
        }

        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment cannot be in the past");
        }

        time = time.withNano(0);

        if (!isDoctorAvailable(doctorId, date, time)) {
            throw new ConflictException(
                    "Doctor already has an appointment at that time"
            );
        }

        if (hasPatientConflict(patientId, date, time)) {
            throw new ConflictException(
                    "Patient already has an appointment at that time"
            );
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepository.save(appointment);
    }

    // --- Cancel appointment ---

    @Transactional
    public void cancelAppointment(int appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException(
                                "Appointment not found with ID: " + appointmentId
                        )
                );

        if (appointment.takeStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("Appointment is already cancelled");
        }

        if (appointment.takeStatus() == AppointmentStatus.FINISHED) {
            throw new IllegalArgumentException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    // --- Get appointment by ID ---

    public Appointment takeAppointmentById(int appointmentId) {

        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException(
                                "Appointment not found with ID: " + appointmentId
                        )
                );
    }

    // --- Check doctor availability ---

    public boolean isDoctorAvailable(int doctorId, LocalDate date, LocalTime time) {

        return !appointmentRepository
                .existsByDoctorDoctorIdAndAppointmentDateAndAppointmentTimeAndStatus(
                        doctorId, date, time, AppointmentStatus.SCHEDULED
                );
    }

    public boolean isDoctorAvailable(int doctorId, String date, String time) {

        LocalDate parsedDate = parseDate(date);
        LocalTime parsedTime = parseTime(time);

        if (parsedDate == null || parsedTime == null) {
            return false;
        }

        return isDoctorAvailable(doctorId, parsedDate, parsedTime);
    }

    // --- Get appointments by patient ---

    public List<Appointment> takeAppointmentsByPatient(int patientId) {

        return appointmentRepository
                .findByPatientPatientIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        patientId
                );
    }

    // --- Get appointments by doctor ---

    public List<Appointment> takeAppointmentsByDoctor(int doctorId) {

        return appointmentRepository
                .findByDoctorDoctorIdOrderByAppointmentDateAscAppointmentTimeAsc(
                        doctorId
                );
    }

    // --- Get today's appointments for a specific doctor ---

    public List<Appointment> takeTodaysAppointmentsByDoctor(int doctorId) {

        return appointmentRepository
                .findByDoctorDoctorIdAndAppointmentDateOrderByAppointmentTimeAsc(
                        doctorId,
                        LocalDate.now()
                );
    }

    // --- Get today's appointments (all doctors) ---

    public List<Appointment> takeTodaysAppointments() {

        return appointmentRepository
                .findByAppointmentDateOrderByAppointmentTimeAsc(
                        LocalDate.now()
                );
    }

    // --- Get all appointments ---

    public List<Appointment> takeAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .sorted(Comparator
                        .comparing(Appointment::takeAppointmentDate)
                        .thenComparing(Appointment::takeAppointmentTime))
                .collect(Collectors.toList());
    }

    // --- Private helpers ---

    private boolean hasPatientConflict(int patientId, LocalDate date, LocalTime time) {

        return appointmentRepository
                .existsByPatientPatientIdAndAppointmentDateAndAppointmentTimeAndStatus(
                        patientId, date, time, AppointmentStatus.SCHEDULED
                );
    }

    private LocalDate parseDate(String date) {
        if (date == null) return null;
        try {
            return LocalDate.parse(date);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    private LocalTime parseTime(String time) {
        if (time == null) return null;
        try {
            return LocalTime.parse(time);
        } catch (RuntimeException exception) {
            try {
                java.time.format.DateTimeFormatter formatter =
                        java.time.format.DateTimeFormatter.ofPattern("h:mm a");
                return LocalTime.parse(time.toUpperCase(), formatter);
            } catch (RuntimeException ignored) {
                throw new IllegalArgumentException("Time is not correct format");
            }
        }
    }
}
