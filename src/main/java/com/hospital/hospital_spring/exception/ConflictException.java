package com.hospital.hospital_spring.exception;

public class ConflictException extends IllegalArgumentException {

    public ConflictException(String message) {
        super(message);
    }
}
