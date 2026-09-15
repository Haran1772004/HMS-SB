package com.hospital.hospital_spring.model;

public class PrescriptionRequest {

    private int recordId;
    private String medicineName;
    private String dosage;
    private String duration;

    public PrescriptionRequest() {
    }

    public PrescriptionRequest(int recordId, String medicineName, String dosage, String duration) {
        this.recordId = recordId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.duration = duration;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
