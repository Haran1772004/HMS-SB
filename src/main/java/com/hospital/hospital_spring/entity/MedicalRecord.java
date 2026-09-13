package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "medical_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_medical_record_appointment",
                        columnNames = "appointment_id"
                )
        }
)
@JsonPropertyOrder({
        "recordId",
        "appointment",
        "diagnosis",
        "treatmentNotes",
        "recordDate"
})
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private int recordId;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true
    )
    private Appointment appointment;


    @Column(name = "diagnosis", nullable = false, columnDefinition = "TEXT")
    private String diagnosis;


    @Column(
            name = "treatment_notes",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String treatmentNotes;


    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    public MedicalRecord() {
    }


    public MedicalRecord(
            int recordId,
            Appointment appointment,
            String diagnosis,
            String treatmentNotes,
            LocalDate recordDate) {

        this.recordId = recordId;
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.treatmentNotes = treatmentNotes;
        this.recordDate = recordDate;
    }

    public MedicalRecord(
            int recordId,
            Appointment appointment,
            String diagnosis,
            String treatmentNotes,
            String recordDate) {

        this.recordId = recordId;
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.treatmentNotes = treatmentNotes;

        if (recordDate != null) {
            this.recordDate = LocalDate.parse(recordDate);
        }
    }

    @JsonProperty("recordId")
    public int takeRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @JsonProperty("appointment")
    public Appointment takeAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @JsonProperty("diagnosis")
    public String takeDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @JsonProperty("treatmentNotes")
    public String takeTreatmentNotes() {
        return treatmentNotes;
    }

    public void setTreatmentNotes(String treatmentNotes) {
        this.treatmentNotes = treatmentNotes;
    }

    @JsonProperty("recordDate")
    public LocalDate takeRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public void setRecordDate(String recordDate) {

        this.recordDate =
                recordDate == null
                        ? null
                        : LocalDate.parse(recordDate);
    }

    @Override
    public String toString() {

        return "MedicalRecord{" +
                "recordId=" + recordId +
                ", appointmentId=" +
                (appointment != null
                        ? appointment.takeAppointmentId()
                        : null) +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatmentNotes='" + treatmentNotes + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }
}