package com.hospital.hospital_spring.model;

import com.hospital.hospital_spring.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponse {

    private int appointmentId;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String departmentName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;

    public AppointmentResponse() {
    }

    public AppointmentResponse(int appointmentId, int patientId, String patientName,
                               int doctorId, String doctorName, String departmentName,
                               LocalDate appointmentDate, LocalTime appointmentTime,
                               AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.departmentName = departmentName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public static AppointmentResponse fromEntity(Appointment appointment) {
        if (appointment == null) return null;
        return new AppointmentResponse(
                appointment.takeAppointmentId(),
                appointment.takePatient() != null ? appointment.takePatient().takePatientId() : 0,
                appointment.takePatient() != null ? appointment.takePatient().takeName() : null,
                appointment.takeDoctor() != null ? appointment.takeDoctor().takeDoctorId() : 0,
                appointment.takeDoctor() != null ? appointment.takeDoctor().takeName() : null,
                (appointment.takeDoctor() != null && appointment.takeDoctor().takeDepartment() != null)
                        ? appointment.takeDoctor().takeDepartment().takeName()
                        : null,
                appointment.takeAppointmentDate(),
                appointment.takeAppointmentTime(),
                appointment.takeStatus()
        );
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
