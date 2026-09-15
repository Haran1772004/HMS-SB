package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.exception.ConflictException;
import com.hospital.hospital_spring.exception.UserNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void joinUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User is required");
        }

        validateUsername(user.takeUsername());
        validatePassword(user.takePassword());

        if (user.takeRole() == null || user.takeRole().isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }

        String normalizedRole = user.takeRole().trim().toUpperCase();
        if (!normalizedRole.equals("ADMIN") &&
                !normalizedRole.equals("DOCTOR") &&
                !normalizedRole.equals("PATIENT") &&
                !normalizedRole.equals("RECEPTIONIST")) {

            throw new IllegalArgumentException(
                    "Invalid role. Accepted: ADMIN, DOCTOR, PATIENT, RECEPTIONIST"
            );
        }
        user.setRole(normalizedRole);

        if (userRepository.existsByUsername(user.takeUsername())) {
            throw new ConflictException(
                    "Username is already taken: " + user.takeUsername()
            );
        }

        // BCrypt encode if not already hashed
        if (!user.takePassword().startsWith("$2a$") &&
                !user.takePassword().startsWith("$2b$") &&
                !user.takePassword().startsWith("$2y$")) {

            user.setPassword(passwordEncoder.encode(user.takePassword()));
        }

        if (user.takeStatus() == null) {
            user.setStatus(AccountStatus.ACTIVE);
        }

        userRepository.save(user);
    }

    public User takeUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found: " + username
                        )
                );
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> takeAllUsers() {
        return userRepository.findAll();
    }

    public List<User> takePendingUsersByRole(String role) {

        return userRepository.findByRoleAndStatus(
                role,
                AccountStatus.PENDING
        );
    }

    public void updateStatus(String username, AccountStatus status) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found: " + username
                        )
                );

        if (status == null) {
            throw new IllegalArgumentException("Account status is required");
        }

        user.setStatus(status);
        userRepository.save(user);
    }

    public void removeUser(String username) {

        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("User not found: " + username);
        }

        userRepository.deleteByUsername(username);
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            throw new IllegalArgumentException(
                    "Username must be 3-20 characters and contain only letters, numbers, and underscores."
            );
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
    }
}
