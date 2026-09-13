package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.exception.DoctorNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;


    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


    // Add doctor
    public void addDoctor(Doctor doctor) {

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor is required");
        }

        if (doctor.takeDepartment() == null) {
            throw new IllegalArgumentException(
                    "Doctor must belong to a department"
            );
        }

        ensureUniqueForAdd(doctor);

        doctorRepository.save(doctor);
    }


    // Update doctor
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

        if (doctor.takeDepartment() == null) {
            throw new IllegalArgumentException(
                    "Doctor must belong to a department"
            );
        }

        ensureUniqueForUpdate(doctor);

        existingDoctor.setName(doctor.takeName());
        existingDoctor.setSpecialization(doctor.takeSpecialization());
        existingDoctor.setPhone(doctor.takePhone());
        existingDoctor.setEmail(doctor.takeEmail());
        existingDoctor.setDepartment(doctor.takeDepartment());
        existingDoctor.setStatus(doctor.takeStatus());
        existingDoctor.setUserId(doctor.takeUserId());

        doctorRepository.save(existingDoctor);
    }


    // Deactivate doctor
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


    // Activate doctor
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


    // Get doctor by ID
    public Doctor takeDoctorById(int doctorId) {

        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with ID: " + doctorId
                        )
                );
    }


    // Get all doctors
    public List<Doctor> takeAllDoctors() {

        return doctorRepository.findAll();
    }


    // -------------------------------------------------
    // Uniqueness validation for ADD
    // -------------------------------------------------

    private void ensureUniqueForAdd(Doctor doctor) {

        if (doctorRepository.existsByEmail(doctor.takeEmail())) {

            throw new IllegalArgumentException(
                    "Doctor with email already exists: "
                            + doctor.takeEmail()
            );
        }

        if (doctorRepository.existsByPhone(doctor.takePhone())) {

            throw new IllegalArgumentException(
                    "Doctor with phone already exists: "
                            + doctor.takePhone()
            );
        }
    }


    // -------------------------------------------------
    // Uniqueness validation for UPDATE
    // -------------------------------------------------

    private void ensureUniqueForUpdate(Doctor doctor) {

        if (doctorRepository.existsByEmailAndDoctorIdNot(
                doctor.takeEmail(),
                doctor.takeDoctorId())) {

            throw new IllegalArgumentException(
                    "Doctor with email already exists: "
                            + doctor.takeEmail()
            );
        }

        if (doctorRepository.existsByPhoneAndDoctorIdNot(
                doctor.takePhone(),
                doctor.takeDoctorId())) {

            throw new IllegalArgumentException(
                    "Doctor with phone already exists: "
                            + doctor.takePhone()
            );
        }
    }
} 
    

