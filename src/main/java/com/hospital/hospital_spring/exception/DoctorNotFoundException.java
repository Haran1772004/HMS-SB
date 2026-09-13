package com.hospital.hospital_spring.exception;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(String message) {
        super(message);
    }
}