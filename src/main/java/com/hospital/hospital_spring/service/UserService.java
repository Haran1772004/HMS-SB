package com.hospital.hospital_spring.service;
import java.util.*;
import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.hospital.hospital_spring.exception.*;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void joinUser(User user) {
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

    user.setStatus(status);
    userRepository.save(user);
}

    public void removeUser(String username) {
        userRepository.deleteByUsername(username);
    }
}