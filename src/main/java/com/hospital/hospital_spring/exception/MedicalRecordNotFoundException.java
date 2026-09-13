package com.hospital.hospital_spring.exception;

public class MedicalRecordNotFoundException
        extends RuntimeException {

    public MedicalRecordNotFoundException(String message) {
        super(message);
    }    
}
