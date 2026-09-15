package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Department;
import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.exception.ConflictException;
import com.hospital.hospital_spring.exception.DepartmentNotFoundException;
import com.hospital.hospital_spring.exception.DoctorNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.DepartmentRepository;
import com.hospital.hospital_spring.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            DepartmentRepository departmentRepository) {

        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public void addDoctor(Doctor doctor) {

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor is required");
        }

        validateDoctorFields(doctor);
        Department activeDepartment = validateAndResolveDepartment(doctor.takeDepartment());
        doctor.setDepartment(activeDepartment);

        ensureUniqueForAdd(doctor);

        if (doctor.takeStatus() == null) {
            doctor.setStatus(AccountStatus.ACTIVE);
        }

        doctorRepository.save(doctor);
    }

    @Transactional
    public void updateDoctor(Doctor doctor) {

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor is required");
        }

        Doctor existingDoctor = doctorRepository.findById(
                doctor.takeDoctorId()
        ).orElseThrow(() ->
                new DoctorNotFoundException(
                        "Doctor not found with ID: "
                                + doctor.takeDoctorId()
                )
        );

        validateDoctorFields(doctor);
        Department activeDepartment = validateAndResolveDepartment(doctor.takeDepartment());

        ensureUniqueForUpdate(doctor);

        existingDoctor.setName(doctor.takeName());
        existingDoctor.setSpecialization(doctor.takeSpecialization());
        existingDoctor.setPhone(doctor.takePhone());
        existingDoctor.setEmail(doctor.takeEmail());
        existingDoctor.setDepartment(activeDepartment);
        if (doctor.takeStatus() != null) {
            existingDoctor.setStatus(doctor.takeStatus());
        }

        doctorRepository.save(existingDoctor);
    }

    @Transactional
    public void deactivateDoctor(int doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with ID: " + doctorId
                        )
                );

        doctor.setStatus(AccountStatus.INACTIVE);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void activateDoctor(int doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with ID: " + doctorId
                        )
                );

        doctor.setStatus(AccountStatus.ACTIVE);
        doctorRepository.save(doctor);
    }

    public Doctor takeDoctorById(int doctorId) {

        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with ID: " + doctorId
                        )
                );
    }

    public List<Doctor> takeAllDoctors() {
        return doctorRepository.findAll();
    }

    private void validateDoctorFields(Doctor doctor) {

        if (doctor.takeName() == null || doctor.takeName().isBlank()) {
            throw new IllegalArgumentException("Doctor name is required");
        }
        if (doctor.takeName().trim().length() < 2 || doctor.takeName().trim().length() > 60) {
            throw new IllegalArgumentException("Doctor name must be 2-60 characters");
        }

        if (doctor.takeSpecialization() == null || doctor.takeSpecialization().isBlank()) {
            throw new IllegalArgumentException("Specialization is required");
        }

        if (doctor.takePhone() != null && !doctor.takePhone().isBlank()) {
            if (!doctor.takePhone().matches("^\\+?[0-9]{7,15}$")) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }

        if (doctor.takeEmail() != null && !doctor.takeEmail().isBlank()) {
            if (!doctor.takeEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    private Department validateAndResolveDepartment(Department dept) {

        if (dept == null || dept.takeDepartmentId() <= 0) {
            throw new IllegalArgumentException("Doctor must belong to a valid department");
        }

        Department department = departmentRepository.findById(dept.takeDepartmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with ID: " + dept.takeDepartmentId()
                        )
                );

        if (department.takeStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "The selected department is not active. Please choose an active department."
            );
        }

        return department;
    }

    private void ensureUniqueForAdd(Doctor doctor) {

        if (doctor.takeEmail() != null && !doctor.takeEmail().isBlank()) {
            if (doctorRepository.existsByEmail(doctor.takeEmail())) {
                throw new ConflictException(
                        "Doctor with email already exists: " + doctor.takeEmail()
                );
            }
        }

        if (doctor.takePhone() != null && !doctor.takePhone().isBlank()) {
            if (doctorRepository.existsByPhone(doctor.takePhone())) {
                throw new ConflictException(
                        "Doctor with phone already exists: " + doctor.takePhone()
                );
            }
        }
    }

    private void ensureUniqueForUpdate(Doctor doctor) {

        if (doctor.takeEmail() != null && !doctor.takeEmail().isBlank()) {
            if (doctorRepository.existsByEmailAndDoctorIdNot(
                    doctor.takeEmail(), doctor.takeDoctorId())) {
                throw new ConflictException(
                        "Doctor with email already exists: " + doctor.takeEmail()
                );
            }
        }

        if (doctor.takePhone() != null && !doctor.takePhone().isBlank()) {
            if (doctorRepository.existsByPhoneAndDoctorIdNot(
                    doctor.takePhone(), doctor.takeDoctorId())) {
                throw new ConflictException(
                        "Doctor with phone already exists: " + doctor.takePhone()
                );
            }
        }
    }
}
