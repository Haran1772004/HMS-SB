package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

@Entity
@Table(name = "prescriptions")
@JsonPropertyOrder({
        "prescriptionId",
        "recordId",
        "medicineName",
        "dosage",
        "duration"
})
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private int prescriptionId;

    @Column(name = "record_id", nullable = false)
    private int recordId;

    @Column(name = "medicine_name", nullable = false, length = 150)
    private String medicineName;

    @Column(name = "dosage", nullable = false, length = 100)
    private String dosage;

    @Column(name = "duration", nullable = false, length = 100)
    private String duration;

    public Prescription() {
    }

    public Prescription(
            int prescriptionId,
            int recordId,
            String medicineName,
            String dosage,
            String duration) {

        this.prescriptionId = prescriptionId;
        this.recordId = recordId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.duration = duration;
    }

    @JsonProperty("prescriptionId")
    public int takePrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    @JsonProperty("recordId")
    public int takeRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @JsonProperty("medicineName")
    public String takeMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    @JsonProperty("dosage")
    public String takeDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @JsonProperty("duration")
    public String takeDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}