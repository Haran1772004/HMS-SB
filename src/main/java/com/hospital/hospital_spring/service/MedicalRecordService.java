package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Appointment;
import com.hospital.hospital_spring.entity.MedicalRecord;
import com.hospital.hospital_spring.exception.AppointmentNotFoundException;
import com.hospital.hospital_spring.exception.DoctorNotFoundException;
import com.hospital.hospital_spring.exception.MedicalRecordNotFoundException;
import com.hospital.hospital_spring.exception.PatientNotFoundException;
import com.hospital.hospital_spring.model.AccountStatus;
import com.hospital.hospital_spring.model.AppointmentStatus;
import com.hospital.hospital_spring.repository.AppointmentRepository;
import com.hospital.hospital_spring.repository.DoctorRepository;
import com.hospital.hospital_spring.repository.MedicalRecordRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;


    public MedicalRecordService(
            MedicalRecordRepository medicalRecordRepository,
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public void createMedicalRecord(MedicalRecord record) {

        if (record == null ||
                record.takeAppointment() == null ||
                record.takeAppointment().takePatient() == null ||
                record.takeAppointment().takeDoctor() == null ||
                record.takeDiagnosis() == null ||
                record.takeDiagnosis().isBlank() ||
                record.takeTreatmentNotes() == null ||
                record.takeTreatmentNotes().isBlank() ||
                record.takeRecordDate() == null) {

            throw new IllegalArgumentException(
                    "Appointment, patient, doctor, diagnosis, treatment notes, and valid record date are required"
            );
        }

        if (!LocalDate.now().equals(
                record.takeRecordDate())) {

            throw new IllegalArgumentException(
                    "Record date must be today"
            );
        }

        int appointmentId =
                record.takeAppointment()
                        .takeAppointmentId();

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found with ID: "
                                                + appointmentId
                                )
                        );
        int patientId =
                appointment.takePatient()
                        .takePatientId();

        patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with ID: "
                                        + patientId
                        )
                );

        int doctorId =
                appointment.takeDoctor()
                        .takeDoctorId();

        doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with ID: "
                                        + doctorId
                        )
                );

        if (medicalRecordRepository
                .existsByAppointmentAppointmentId(
                        appointmentId)) {

            throw new IllegalArgumentException(
                    "Medical record already exists for this appointment"
            );
        }

        record.setAppointment(appointment);

        medicalRecordRepository.save(record);

        appointment.setStatus(AppointmentStatus.FINISHED);

        appointmentRepository.save(appointment);
    }

    public MedicalRecord takeRecordById(int recordId) {

        return medicalRecordRepository
                .findById(recordId)
                .orElseThrow(() ->
                        new MedicalRecordNotFoundException(
                                "Medical record not found with ID: "
                                        + recordId
                        )
                );
    }

    public List<MedicalRecord> takeRecordsByPatient(
            int patientId) {

        return medicalRecordRepository
                .findByAppointmentPatientPatientIdOrderByRecordDateDescRecordIdDesc(
                        patientId
                );
    }

    public List<MedicalRecord> takeRecordsByDoctor(
            int doctorId) {

        return medicalRecordRepository
                .findByAppointmentDoctorDoctorIdOrderByRecordDateDescRecordIdDesc(
                        doctorId
                );
    }

    public List<MedicalRecord> takeAllRecords() {

        return medicalRecordRepository
                .findAllByOrderByRecordDateDescRecordIdDesc();
    }
}