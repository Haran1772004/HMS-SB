package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.MedicalRecord;
import com.hospital.hospital_spring.entity.Prescription;
import com.hospital.hospital_spring.repository.MedicalRecordRepository;
import com.hospital.hospital_spring.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            MedicalRecordRepository medicalRecordRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Transactional
    public void addPrescription(Prescription prescription) {

        if (prescription == null
                || prescription.takeRecordId() <= 0
                || !isNonBlank(prescription.takeMedicineName())
                || !isNonBlank(prescription.takeDosage())
                || !isNonBlank(prescription.takeDuration())) {

            throw new IllegalArgumentException(
                    "Medical record, medicine name, dosage, and duration are required");
        }

        if (!medicalRecordRepository.existsById(prescription.takeRecordId())) {
            throw new IllegalArgumentException(
                    "Medical record does not exist");
        }

        prescriptionRepository.save(prescription);
    }

    public List<Prescription> takePrescriptionsByRecord(int recordId) {

        if (recordId <= 0) {
            throw new IllegalArgumentException(
                    "Medical record is required");
        }

        return prescriptionRepository
                .findByRecordIdOrderByPrescriptionId(recordId);
    }

    public List<Prescription> takePrescriptionsByPatient(int patientId) {

        List<MedicalRecord> records = medicalRecordRepository
                .findByAppointmentPatientPatientIdOrderByRecordDateDescRecordIdDesc(patientId);

        if (records.isEmpty()) {
            return List.of();
        }

        List<Integer> recordIds = records.stream()
                .map(MedicalRecord::takeRecordId)
                .collect(Collectors.toList());

        return prescriptionRepository
                .findByRecordIdInOrderByPrescriptionId(recordIds);
    }

    private boolean isNonBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
