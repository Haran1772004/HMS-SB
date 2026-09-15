package com.hospital.hospital_spring.model;

import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.entity.PatientAddress;

import java.util.List;

public class PatientProfileResponse {

    private Patient patient;
    private List<PatientAddress> addresses;

    public PatientProfileResponse() {
    }

    public PatientProfileResponse(Patient patient, List<PatientAddress> addresses) {
        this.patient = patient;
        this.addresses = addresses;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<PatientAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<PatientAddress> addresses) {
        this.addresses = addresses;
    }
}
