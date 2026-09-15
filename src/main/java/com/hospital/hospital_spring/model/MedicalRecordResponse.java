package com.hospital.hospital_spring.model;

import com.hospital.hospital_spring.entity.MedicalRecord;

import java.time.LocalDate;

public class MedicalRecordResponse {

    private int recordId;
    private int appointmentId;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String diagnosis;
    private String treatmentNotes;
    private LocalDate recordDate;

    public MedicalRecordResponse() {
    }

    public MedicalRecordResponse(int recordId, int appointmentId, int patientId,
                                 String patientName, int doctorId, String doctorName,
                                 String diagnosis, String treatmentNotes, LocalDate recordDate) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.treatmentNotes = treatmentNotes;
        this.recordDate = recordDate;
    }

    public static MedicalRecordResponse fromEntity(MedicalRecord record) {
        if (record == null) return null;
        int apptId = record.takeAppointment() != null ? record.takeAppointment().takeAppointmentId() : 0;
        int patId = (record.takeAppointment() != null && record.takeAppointment().takePatient() != null)
                ? record.takeAppointment().takePatient().takePatientId() : 0;
        String patName = (record.takeAppointment() != null && record.takeAppointment().takePatient() != null)
                ? record.takeAppointment().takePatient().takeName() : null;
        int docId = (record.takeAppointment() != null && record.takeAppointment().takeDoctor() != null)
                ? record.takeAppointment().takeDoctor().takeDoctorId() : 0;
        String docName = (record.takeAppointment() != null && record.takeAppointment().takeDoctor() != null)
                ? record.takeAppointment().takeDoctor().takeName() : null;

        return new MedicalRecordResponse(
                record.takeRecordId(),
                apptId,
                patId,
                patName,
                docId,
                docName,
                record.takeDiagnosis(),
                record.takeTreatmentNotes(),
                record.takeRecordDate()
        );
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
