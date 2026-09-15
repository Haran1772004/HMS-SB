package com.hospital.hospital_spring.model;

import java.time.LocalDate;

public class MedicalRecordRequest {

    private int appointmentId;
    private String diagnosis;
    private String treatmentNotes;
    private LocalDate recordDate;

    public MedicalRecordRequest() {
    }

    public MedicalRecordRequest(int appointmentId, String diagnosis,
                                String treatmentNotes, LocalDate recordDate) {
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.treatmentNotes = treatmentNotes;
        this.recordDate = recordDate;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentNotes() {
        return treatmentNotes;
    }

    public void setTreatmentNotes(String treatmentNotes) {
        this.treatmentNotes = treatmentNotes;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}
