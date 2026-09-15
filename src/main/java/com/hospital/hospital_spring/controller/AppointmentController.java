package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.exception.AuthorizationException;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.security.CurrentUser;
import com.hospital.hospital_spring.service.AppointmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentController(
            AppointmentService appointmentService,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PostMapping
    public void bookAppointment(@RequestBody Appointment appointment) {
        appointmentService.bookAppointment(appointment);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/{appointmentId}/cancel")
    public void cancelAppointment(@PathVariable int appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
    }

  
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/{appointmentId}")
    public Appointment getAppointmentById(@PathVariable int appointmentId) {

        Appointment appointment =
                appointmentService.takeAppointmentById(appointmentId);

        if (isDoctorRole()) {
            int appointmentDoctorId =
                    appointment.takeDoctor().takeDoctorId();
            ensureOwnDoctor(appointmentDoctorId);
        }

        return appointment;
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/doctor/{doctorId}/available")
    public boolean isDoctorAvailable(
            @PathVariable int doctorId,
            @RequestParam String date,
            @RequestParam String time) {

        return appointmentService.isDoctorAvailable(doctorId, date, time);
    }

  
     
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatient(
            @PathVariable int patientId) {

        if (isPatientRole()) {
            ensureOwnPatient(patientId);
        }

        return appointmentService.takeAppointmentsByPatient(patientId);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(
            @PathVariable int doctorId) {

        if (isDoctorRole()) {
            ensureOwnDoctor(doctorId);
        }

        return appointmentService.takeAppointmentsByDoctor(doctorId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping("/doctor/{doctorId}/today")
    public List<Appointment> getTodaysAppointmentsByDoctor(
            @PathVariable int doctorId) {

        if (isDoctorRole()) {
            ensureOwnDoctor(doctorId);
        }

        return appointmentService.takeTodaysAppointmentsByDoctor(doctorId);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping("/today")
    public List<Appointment> getTodaysAppointments() {
        return appointmentService.takeTodaysAppointments();
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.takeAllAppointments();
    }

   
    private boolean isPatientRole() {
        return "ROLE_PATIENT".equals(CurrentUser.getRole());
    }

    private boolean isDoctorRole() {
        return "ROLE_DOCTOR".equals(CurrentUser.getRole());
    }

    private void ensureOwnPatient(int requestedPatientId) {

        int currentUserId = CurrentUser.getUserId();

        Patient ownPatient = patientRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No patient profile linked to your account."
                        )
                );

        if (ownPatient.takePatientId() != requestedPatientId) {
            throw new AuthorizationException(
                    "Access denied: you can only view your own appointments."
            );
        }
    }

    private void ensureOwnDoctor(int requestedDoctorId) {

        int currentUserId = CurrentUser.getUserId();

        Doctor ownDoctor = doctorRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No doctor profile linked to your account."
                        )
                );

        if (ownDoctor.takeDoctorId() != requestedDoctorId) {
            throw new AuthorizationException(
                    "Access denied: you can only view your own appointments."
            );
        }
    }
}
