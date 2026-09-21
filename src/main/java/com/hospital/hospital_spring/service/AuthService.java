package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.exception.AuthenticationException;
import com.hospital.hospital_spring.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        } 
        
        catch (org.springframework.security.core.AuthenticationException exception) {

            throw new AuthenticationException("Invalid username or password.");
        }

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new AuthenticationException("Invalid username or password."));

        if (user.takeStatus() == null) {

            throw new AuthenticationException(
                    "Invalid account status."
            );
        }


        if (user.takeStatus()
                .name()
                .equals("PENDING")) {

            throw new AuthenticationException(
                    "Your account is awaiting admin approval. "
                    + "Please try again later."
            );
        }


        if (user.takeStatus()
                .name()
                .equals("REJECTED")) {

            throw new AuthenticationException(
                    "Your account registration was rejected."
            );
        }
        return jwtService.generateToken(user);
    }
}