package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.model.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientAddressRepository
        extends JpaRepository<PatientAddress, Integer> {

    List<PatientAddress> findByPatientIdOrderByAddressType(int patientId);

    Optional<PatientAddress> findFirstByPatientIdAndAddressTypeOrderByAddressIdAsc(
            int patientId,
            AddressType addressType
    );

    Optional<PatientAddress> findFirstByPatientIdOrderByAddressIdAsc(
            int patientId
    );

    boolean existsByPatientIdAndAddressType(
            int patientId,
            AddressType addressType
    );

    boolean existsByPatientIdAndAddressTypeAndAddressIdNot(
            int patientId,
            AddressType addressType,
            int addressId
    );

    void deleteByPatientId(int patientId);
}
