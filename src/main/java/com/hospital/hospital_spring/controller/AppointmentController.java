package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;


    public AppointmentController(
            AppointmentService appointmentService) {

        this.appointmentService = appointmentService;
    }

    @PostMapping
    public void bookAppointment(@RequestBody Appointment appointment) {

        appointmentService.bookAppointment(appointment);
    }

    @PutMapping("/{appointmentId}/cancel")
    public void cancelAppointment( @PathVariable int appointmentId) {

        appointmentService.cancelAppointment(
                appointmentId
        );
    }

    @GetMapping("/doctor/{doctorId}/available")
    public boolean isDoctorAvailable(
            @PathVariable int doctorId,
            @RequestParam String date,
            @RequestParam String time) {

        return appointmentService.isDoctorAvailable(
                doctorId,
                date,
                time
        );
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatient(
            @PathVariable int patientId) {

        return appointmentService
                .takeAppointmentsByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(
            @PathVariable int doctorId) {

        return appointmentService
                .takeAppointmentsByDoctor(doctorId);
    }

    @GetMapping("/today")
    public List<Appointment> getTodaysAppointments() {

        return appointmentService
                .takeTodaysAppointments();
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {

        return appointmentService
                .takeAllAppointments();
    }
}