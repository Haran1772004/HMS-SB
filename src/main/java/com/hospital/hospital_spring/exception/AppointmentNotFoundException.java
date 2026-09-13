package com.hospital.hospital_spring.exception;

public class AppointmentNotFoundException
        extends RuntimeException {

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}