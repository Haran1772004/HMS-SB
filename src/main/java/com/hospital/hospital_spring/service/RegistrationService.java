package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Department;
import com.hospital.hospital_spring.entity.Doctor;
import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.exception.ConflictException;
import com.hospital.hospital_spring.exception.DepartmentNotFoundException;
import com.hospital.hospital_spring.model.*;
import com.hospital.hospital_spring.repository.DepartmentRepository;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.PatientAddressRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PatientAddressRepository patientAddressRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            DepartmentRepository departmentRepository,
            PatientAddressRepository patientAddressRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.patientAddressRepository = patientAddressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerPatient(PatientRegistrationRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Registration request is required.");
        }

        // 1. Validate all fields
        validateUsernameField(request.getUsername());
        validatePasswordField(request.getPassword());
        validateNameField(request.getName(), "Patient name");
        LocalDate dob = validateAndParseDob(request.getDob());
        Gender gender = validateAndParseGender(request.getGender());
        validatePhoneField(request.getPhone());
        validateEmailField(request.getEmail());

        // 2. Check username uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException(
                    "Username is already taken: " + request.getUsername()
            );
        }

        // 3. Check patient phone/email uniqueness
        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException(
                    "A patient with this phone number already exists."
            );
        }

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "A patient with this email already exists."
            );
        }

        // 4. BCrypt-encode the password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 5. Create and save the User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setRole("PATIENT");
        user.setStatus(AccountStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        // 6. Create and save the Patient profile
        Patient patient = new Patient();
        patient.setUserId(savedUser.takeUserId());
        patient.setName(request.getName());
        patient.setDob(dob);
        patient.setGender(gender);
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setStatus(AccountStatus.ACTIVE);

        Patient savedPatient = patientRepository.save(patient);

        // 7. Optionally save a PatientAddress
        boolean hasAddress =
                isNonBlank(request.getState())
                || isNonBlank(request.getDistrict())
                || isNonBlank(request.getPincode());

        if (hasAddress) {
            validateAddressFields(request);
            AddressType addressType = parseAddressType(request.getAddressType());

            PatientAddress address = new PatientAddress();
            address.setPatientId(savedPatient.takePatientId());
            address.setState(request.getState());
            address.setDistrict(request.getDistrict());
            address.setPincode(request.getPincode());
            address.setAddressType(addressType);

            patientAddressRepository.save(address);
        }
    }

    @Transactional
    public void registerDoctor(DoctorRegistrationRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Registration request is required.");
        }

        // 1. Validate all fields
        validateUsernameField(request.getUsername());
        validatePasswordField(request.getPassword());
        validateNameField(request.getName(), "Doctor name");
        validateSpecializationField(request.getSpecialization());
        validatePhoneField(request.getPhone());
        validateEmailField(request.getEmail());
        validateDepartmentId(request.getDepartmentId());

        // 2. Check username uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException(
                    "Username is already taken: " + request.getUsername()
            );
        }

        // 3. Check doctor phone/email uniqueness
        if (doctorRepository.existsByPhone(request.getPhone())) {
            throw new ConflictException(
                    "A doctor with this phone number already exists."
            );
        }

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "A doctor with this email already exists."
            );
        }

        // 4. Verify the department exists
        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with ID: "
                                        + request.getDepartmentId()
                        )
                );

        // 5. Verify the department is ACTIVE
        if (department.takeStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "The selected department is not active. "
                    + "Please choose an active department."
            );
        }

        // 6. BCrypt-encode the password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 7. Create and save the User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setRole("DOCTOR");
        user.setStatus(AccountStatus.PENDING);

        User savedUser = userRepository.save(user);

        // 8. Create and save the Doctor profile
        Doctor doctor = new Doctor();
        doctor.setUserId(savedUser.takeUserId());
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setDepartment(department);
        doctor.setStatus(AccountStatus.PENDING);

        doctorRepository.save(doctor);
    }

    // =========================================================================
    // Private validation helpers
    // =========================================================================

    private void validateUsernameField(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            throw new IllegalArgumentException(
                    "Username must be 3-20 characters and contain only letters, numbers, and underscores."
            );
        }
    }

    private void validatePasswordField(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
    }

    private void validateNameField(String name, String fieldLabel) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldLabel + " is required.");
        }
        if (name.trim().length() < 2 || name.trim().length() > 60) {
            throw new IllegalArgumentException(fieldLabel + " must be 2-60 characters.");
        }
    }

    private void validateSpecializationField(String specialization) {
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization is required.");
        }
        if (specialization.trim().length() < 2) {
            throw new IllegalArgumentException("Specialization must be at least 2 characters.");
        }
    }

    private void validatePhoneField(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required.");
        }
        if (!phone.matches("^\\+?[0-9]{7,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    private void validateEmailField(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    private void validateDepartmentId(int departmentId) {
        if (departmentId <= 0) {
            throw new IllegalArgumentException("A valid department ID is required.");
        }
    }

    private LocalDate validateAndParseDob(String dob) {
        if (dob == null || dob.isBlank()) {
            throw new IllegalArgumentException("Date of birth is required.");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(dob, formatter);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Invalid date of birth. Expected format: uuuu-MM-dd"
            );
        }
    }

    private Gender validateAndParseGender(String gender) {
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender is required.");
        }
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid gender. Accepted: MALE, FEMALE, OTHER.");
        }
    }

    private void validateAddressFields(PatientRegistrationRequest request) {
        if (!isNonBlank(request.getState())) {
            throw new IllegalArgumentException("State is required when providing an address.");
        }
        if (!isNonBlank(request.getDistrict())) {
            throw new IllegalArgumentException("District is required when providing an address.");
        }
        if (!isNonBlank(request.getPincode())) {
            throw new IllegalArgumentException("Pincode is required when providing an address.");
        }
    }

    private AddressType parseAddressType(String addressType) {
        if (addressType == null || addressType.isBlank()) {
            return AddressType.HOME;
        }
        try {
            return AddressType.valueOf(addressType.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid address type. Accepted: HOME, WORK, BILLING, EMERGENCY."
            );
        }
    }

    private boolean isNonBlank(String value) {
        return value != null && !value.isBlank();
    }
}
