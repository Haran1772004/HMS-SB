package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hospital.hospital_spring.model.AccountStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
@JsonPropertyOrder({
        "doctorId",
        "userId",
        "name",
        "specialization",
        "phone",
        "email",
        "department",
        "status",
        "statusName"
})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private int doctorId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;


    public Doctor() {
    }


    public Doctor(
            String name,
            String specialization,
            String phone,
            String email,
            Department department) {

        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.status = AccountStatus.PENDING;
    }


    @JsonProperty("doctorId")
    public int takeDoctorId() {
        return doctorId;
    }

    @JsonProperty("userId")
    public Integer takeUserId() {
        return userId;
    }

    @JsonProperty("name")
    public String takeName() {
        return name;
    }

    @JsonProperty("specialization")
    public String takeSpecialization() {
        return specialization;
    }

    @JsonProperty("phone")
    public String takePhone() {
        return phone;
    }

    @JsonProperty("email")
    public String takeEmail() {
        return email;
    }

    @JsonProperty("department")
    public Department takeDepartment() {
        return department;
    }

    @JsonProperty("status")
    public AccountStatus takeStatus() {
        return status;
    }

    @JsonProperty("statusName")
    public String takeStatusName() {
        return status == null ? null : status.name();
    }


    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}