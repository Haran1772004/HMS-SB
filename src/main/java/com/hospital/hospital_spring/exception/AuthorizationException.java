package com.hospital.hospital_spring.exception;

/**
 * Thrown when an authenticated user attempts to access
 * a resource they do not own or are not permitted to see.
 *
 * Maps to HTTP 403 Forbidden.
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String message) {
        super(message);
    }
}
