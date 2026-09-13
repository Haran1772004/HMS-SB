package com.hospital.hospital_spring.exception;

public class PatientAddressNotFoundException
        extends RuntimeException {

    public PatientAddressNotFoundException(String message) {
        super(message);
    }
}