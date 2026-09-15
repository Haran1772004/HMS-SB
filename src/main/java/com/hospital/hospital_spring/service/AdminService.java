package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.exception.DoctorNotFoundException;
import com.hospital.hospital_spring.exception.UserNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.PatientAddressRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PatientAddressRepository patientAddressRepository;

    public AdminService(
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            PatientAddressRepository patientAddressRepository) {

        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.patientAddressRepository = patientAddressRepository;
    }

    /**
     * Approves a pending doctor registration in a single transaction:
     *   1. Finds the User by username
     *   2. Sets User status = ACTIVE
     *   3. Finds the Doctor profile linked to that User
     *   4. Sets Doctor status = ACTIVE
     */
    @Transactional
    public void approveDoctor(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found: " + username
                        )
                );

        if (!"DOCTOR".equals(user.takeRole())) {
            throw new IllegalArgumentException(
                    "User '" + username + "' is not a doctor."
            );
        }

        if (user.takeStatus() != AccountStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Doctor '" + username + "' is not in PENDING status. "
                    + "Current status: " + user.takeStatus()
            );
        }

        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        Doctor doctor = doctorRepository
                .findByUserId(user.takeUserId())
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor profile not found for user: " + username
                        )
                );

        doctor.setStatus(AccountStatus.ACTIVE);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void rejectDoctor(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found: " + username
                        )
                );

        if (!"DOCTOR".equals(user.takeRole())) {
            throw new IllegalArgumentException(
                    "User '" + username + "' is not a doctor."
            );
        }

        if (user.takeStatus() != AccountStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Doctor '" + username + "' is not in PENDING status. "
                    + "Current status: " + user.takeStatus()
            );
        }

        user.setStatus(AccountStatus.REJECTED);
        userRepository.save(user);

        doctorRepository.findByUserId(user.takeUserId())
                .ifPresent(doctor -> {
                    doctor.setStatus(AccountStatus.REJECTED);
                    doctorRepository.save(doctor);
                });
    }

   
    @Transactional
    public void removeUserAndProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found: " + username));

        if ("DOCTOR".equals(user.takeRole())) {
            doctorRepository.findByUserId(user.takeUserId())
                    .ifPresent(doctorRepository::delete);
        } else if ("PATIENT".equals(user.takeRole())) {
            patientRepository.findByUserId(user.takeUserId())
                    .ifPresent(patient -> {
                        patientAddressRepository.deleteByPatientId(patient.takePatientId());
                        patientRepository.delete(patient);
                    });
        }

        userRepository.delete(user);
    }

    public List<Doctor> takePendingDoctors() {

        return doctorRepository.findByStatus(AccountStatus.PENDING);
    }
}
