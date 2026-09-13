package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hospital.hospital_spring.model.AppointmentStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@JsonPropertyOrder({
        "appointmentId",
        "patient",
        "doctor",
        "appointmentDate",
        "appointmentTime",
        "status"
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int appointmentId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;


    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;


    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    public Appointment() {
    }


    public Appointment(
            int appointmentId,
            Patient patient,
            Doctor doctor,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            AppointmentStatus status) {

        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }
//just for understanding the string input for appointment date and time ...
    public Appointment(
            int appointmentId,
            Patient patient,
            Doctor doctor,
            String appointmentDate,
            String appointmentTime,
            AppointmentStatus status) {

        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;

        if (appointmentDate != null) {
            this.appointmentDate = LocalDate.parse(appointmentDate);
        }

        if (appointmentTime != null) {
            this.appointmentTime = LocalTime.parse(appointmentTime);
        }

        this.status = status;
    }

    public Appointment(
            int appointmentId,
            Patient patient,
            Doctor doctor,
            String appointmentDate,
            String appointmentTime,
            String status) {

        this(
                appointmentId,
                patient,
                doctor,
                appointmentDate,
                appointmentTime,
                status == null
                        ? null
                        : AppointmentStatus.valueOf(
                                status.toUpperCase()
                        )
        );
    }

    @JsonProperty("appointmentId")
    public int takeAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    @JsonProperty("patient")
    public Patient takePatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @JsonProperty("doctor")
    public Doctor takeDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @JsonProperty("appointmentDate")
    public LocalDate takeAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {

        this.appointmentDate =
                appointmentDate == null
                        ? null
                        : LocalDate.parse(appointmentDate);
    }

    @JsonProperty("appointmentTime")
    public LocalTime takeAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {

        this.appointmentTime =
                appointmentTime == null
                        ? null
                        : LocalTime.parse(appointmentTime);
    }

    @JsonProperty("status")
    public AppointmentStatus takeStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {

        this.status =
                status == null
                        ? null
                        : AppointmentStatus.valueOf(
                                status.toUpperCase()
                        );
    }

    @Override
    public String toString() {

        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patient=" +
                (patient != null
                        ? patient.takeName()
                        : null) +
                ", doctor=" +
                (doctor != null
                        ? doctor.takeName()
                        : null) +
                ", appointmentDate=" +
                appointmentDate +
                ", appointmentTime=" +
                appointmentTime +
                ", status=" +
                status +
                '}';
    }
}