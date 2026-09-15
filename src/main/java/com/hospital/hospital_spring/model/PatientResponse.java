package com.hospital.hospital_spring.model;

import com.hospital.hospital_spring.entity.Patient;

import java.time.LocalDate;

public class PatientResponse {

    private int patientId;
    private String name;
    private LocalDate dob;
    private Gender gender;
    private String phone;
    private String email;
    private AccountStatus status;

    public PatientResponse() {
    }

    public PatientResponse(int patientId, String name, LocalDate dob,
                           Gender gender, String phone, String email, AccountStatus status) {
        this.patientId = patientId;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    public static PatientResponse fromEntity(Patient patient) {
        if (patient == null) return null;
        return new PatientResponse(
                patient.takePatientId(),
                patient.takeName(),
                patient.takeDob(),
                patient.takeGender(),
                patient.takePhone(),
                patient.takeEmail(),
                patient.takeStatus()
        );
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
