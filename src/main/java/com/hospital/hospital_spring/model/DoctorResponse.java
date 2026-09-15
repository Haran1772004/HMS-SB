package com.hospital.hospital_spring.model;

import com.hospital.hospital_spring.entity.Doctor;

public class DoctorResponse {

    private int doctorId;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private int departmentId;
    private String departmentName;
    private AccountStatus status;

    public DoctorResponse() {
    }

    public DoctorResponse(int doctorId, String name, String specialization,
                          String phone, String email, int departmentId,
                          String departmentName, AccountStatus status) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.status = status;
    }

    public static DoctorResponse fromEntity(Doctor doctor) {
        if (doctor == null) return null;
        return new DoctorResponse(
                doctor.takeDoctorId(),
                doctor.takeName(),
                doctor.takeSpecialization(),
                doctor.takePhone(),
                doctor.takeEmail(),
                doctor.takeDepartment() != null ? doctor.takeDepartment().takeDepartmentId() : 0,
                doctor.takeDepartment() != null ? doctor.takeDepartment().takeName() : null,
                doctor.takeStatus()
        );
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
