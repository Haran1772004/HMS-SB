package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Integer> {

    List<Prescription> findByRecordIdOrderByPrescriptionId(int recordId);

    List<Prescription> findByRecordIdInOrderByPrescriptionId(List<Integer> recordIds);
}
