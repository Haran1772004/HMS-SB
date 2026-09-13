package com.hospital.hospital_spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(
            UserNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }


    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<String> handlePatientNotFound(
            PatientNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }


    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<String> handleDoctorNotFound(
            DoctorNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

       @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<String> handleDepartmentNotFound(
            DepartmentNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(PatientAddressNotFoundException.class)
public ResponseEntity<String> handlePatientAddressNotFound(
        PatientAddressNotFoundException exception) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
}

@ExceptionHandler(AppointmentNotFoundException.class)
public ResponseEntity<String> handleAppointmentNotFound(
        AppointmentNotFoundException exception) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
}

@ExceptionHandler(MedicalRecordNotFoundException.class)
public ResponseEntity<String> handleMedicalRecordNotFound(
        MedicalRecordNotFoundException exception) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
}

@ExceptionHandler(AuthenticationException.class)
public ResponseEntity<String> handleAuthenticationException(
        AuthenticationException exception) {

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(exception.getMessage());
}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
    
}