package com.hospital.hospital_spring.model;

/**
 * DTO for the doctor self-registration endpoint.
 *
 * POST /auth/register/doctor
 *
 * The client sends all fields needed to create:
 *   - a User account (username, password, role=DOCTOR, status=PENDING)
 *   - a Doctor profile (name, specialization, phone, email, departmentId)
 *
 * Doctors require admin approval before they can log in.
 * Their account status starts as PENDING.
 */
public class DoctorRegistrationRequest {

    // User account fields
    private String username;
    private String password;

    // Doctor profile fields
    private String name;
    private String specialization;
    private String phone;
    private String email;

    // Department the doctor belongs to (must exist and be ACTIVE)
    private int departmentId;

    public DoctorRegistrationRequest() {
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
}
