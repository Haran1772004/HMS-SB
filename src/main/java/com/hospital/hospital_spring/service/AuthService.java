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

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String login(
            String username,
            String password) {

        try {

            // Spring Security performs:
            //
            // 1. Find user
            // 2. Load password
            // 3. Compare BCrypt password
            // 4. Verify authentication
            //
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

        } catch (org.springframework.security.core.AuthenticationException exception) {

            throw new AuthenticationException(
                    "Invalid username or password."
            );
        }


        // Authentication succeeded.
        // Now get our actual User entity.

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new AuthenticationException(
                                "Invalid username or password."
                        )
                );


        // Keep your old account-status behavior.

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


        // Authentication + status checks succeeded.
        // Generate JWT.

        return jwtService.generateToken(user);
    }
}