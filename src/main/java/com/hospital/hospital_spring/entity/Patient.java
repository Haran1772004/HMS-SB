package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.model.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@JsonPropertyOrder({
        "patientId",
        "userId",
        "name",
        "dob",
        "gender",
        "phone",
        "email",
        "status",
        "statusName"
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int patientId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    public Patient() {
    }

    public Patient(String name,
                    LocalDate dob,
                    Gender gender,
                    String phone,
                    String email) {

        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.status = AccountStatus.PENDING;
    }

    @JsonProperty("patientId")
    public int takePatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @JsonProperty("userId")
    public Integer takeUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("name")
    public String takeName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("dob")
    public LocalDate takeDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @JsonProperty("gender")
    public Gender takeGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @JsonProperty("phone")
    public String takePhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("email")
    public String takeEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("status")
    public AccountStatus takeStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @JsonProperty("statusName")
    public String takeStatusName() {
        return status == null ? null : status.name();
    }
}