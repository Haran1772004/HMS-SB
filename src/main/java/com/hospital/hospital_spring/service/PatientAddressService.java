package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.exception.ConflictException;
import com.hospital.hospital_spring.exception.PatientAddressNotFoundException;
import com.hospital.hospital_spring.exception.PatientNotFoundException;
import com.hospital.hospital_spring.model.AddressType;
import com.hospital.hospital_spring.repository.PatientAddressRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientAddressService {

    private final PatientAddressRepository patientAddressRepository;
    private final PatientRepository patientRepository;

    public PatientAddressService(
            PatientAddressRepository patientAddressRepository,
            PatientRepository patientRepository) {

        this.patientAddressRepository = patientAddressRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public void addAddress(PatientAddress address) {

        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }

        ensureRequiredFields(address);
        ensurePatientExists(address.takePatientId());

        if (address.takeAddressType() == null) {
            address.setAddressType(AddressType.HOME);
        }

        ensureUniqueAddressTypeForAdd(address);

        patientAddressRepository.save(address);
    }

    @Transactional
    public void updateAddress(PatientAddress address) {

        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }

        PatientAddress existingAddress =
                patientAddressRepository.findById(
                        address.takeAddressId()
                ).orElseThrow(() ->
                        new PatientAddressNotFoundException(
                                "Address not found with ID: "
                                        + address.takeAddressId()
                        )
                );

        ensureRequiredFields(address);
        ensurePatientExists(address.takePatientId());

        if (address.takeAddressType() == null) {
            address.setAddressType(AddressType.HOME);
        }

        ensureUniqueAddressTypeForUpdate(address);

        existingAddress.setPatientId(address.takePatientId());
        existingAddress.setState(address.takeState());
        existingAddress.setDistrict(address.takeDistrict());
        existingAddress.setPincode(address.takePincode());
        existingAddress.setAddressType(address.takeAddressType());

        patientAddressRepository.save(existingAddress);
    }

    @Transactional
    public void removeAddress(int addressId) {

        patientAddressRepository.findById(addressId)
                .orElseThrow(() ->
                        new PatientAddressNotFoundException(
                                "Address not found with ID: "
                                        + addressId
                        )
                );

        patientAddressRepository.deleteById(addressId);
    }

    public PatientAddress takeAddressById(int addressId) {

        return patientAddressRepository.findById(addressId)
                .orElseThrow(() ->
                        new PatientAddressNotFoundException(
                                "Address not found with ID: "
                                        + addressId
                        )
                );
    }

    public List<PatientAddress> takeAddressesByPatient(int patientId) {

        return patientAddressRepository
                .findByPatientIdOrderByAddressType(patientId);
    }

    public List<PatientAddress> takeAllAddresses() {

        return patientAddressRepository.findAll();
    }

    public PatientAddress takeDefaultAddressForPatient(int patientId) {

        return patientAddressRepository
                .findFirstByPatientIdAndAddressTypeOrderByAddressIdAsc(
                        patientId,
                        AddressType.HOME
                )
                .orElseGet(() ->
                        patientAddressRepository
                                .findFirstByPatientIdOrderByAddressIdAsc(
                                        patientId
                                )
                                .orElse(null)
                );
    }

    private void ensurePatientExists(int patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(
                    "Patient not found with ID: " + patientId
            );
        }
    }

    private void ensureRequiredFields(PatientAddress address) {

        if (address.takeState() == null || address.takeState().isBlank()) {
            throw new IllegalArgumentException("State is required");
        }

        if (address.takeDistrict() == null || address.takeDistrict().isBlank()) {
            throw new IllegalArgumentException("District is required");
        }

        if (address.takePincode() == null || address.takePincode().isBlank()) {
            throw new IllegalArgumentException("Pincode is required");
        }

        if (address.takePatientId() <= 0) {
            throw new IllegalArgumentException("Valid patient ID is required");
        }
    }

    private void ensureUniqueAddressTypeForAdd(PatientAddress address) {

        if (patientAddressRepository.existsByPatientIdAndAddressType(
                address.takePatientId(), address.takeAddressType())) {

            throw new ConflictException(
                    "Patient already has an address of type: "
                            + address.takeAddressType()
            );
        }
    }

    private void ensureUniqueAddressTypeForUpdate(PatientAddress address) {

        if (patientAddressRepository.existsByPatientIdAndAddressTypeAndAddressIdNot(
                address.takePatientId(), address.takeAddressType(), address.takeAddressId())) {

            throw new ConflictException(
                    "Patient already has an address of type: "
                            + address.takeAddressType()
            );
        }
    }
}
