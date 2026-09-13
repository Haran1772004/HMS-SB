package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.exception.PatientAddressNotFoundException;
import com.hospital.hospital_spring.model.AddressType;
import com.hospital.hospital_spring.repository.PatientAddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAddressService {

    private final PatientAddressRepository patientAddressRepository;

    public PatientAddressService(
            PatientAddressRepository patientAddressRepository) {

        this.patientAddressRepository = patientAddressRepository;
    }



    public void addAddress(PatientAddress address) {

        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }

        if (address.takeAddressType() == null) {
            address.setAddressType(AddressType.HOME);
        }

        ensureRequiredFields(address);

        ensureUniqueAddressTypeForAdd(address);

        patientAddressRepository.save(address);
    }

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

        if (address.takeAddressType() == null) {
            address.setAddressType(AddressType.HOME);
        }

        ensureRequiredFields(address);

        ensureUniqueAddressTypeForUpdate(address);

        existingAddress.setPatientId(
                address.takePatientId()
        );

        existingAddress.setState(
                address.takeState()
        );

        existingAddress.setDistrict(
                address.takeDistrict()
        );

        existingAddress.setPincode(
                address.takePincode()
        );

        existingAddress.setAddressType(
                address.takeAddressType()
        );

        patientAddressRepository.save(existingAddress);
    }


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


    

    public List<PatientAddress> takeAddressesByPatient(
            int patientId) {

        return patientAddressRepository
                .findByPatientIdOrderByAddressType(patientId);
    }


    
    public List<PatientAddress> takeAllAddresses() {

        return patientAddressRepository.findAll();
    }


    public PatientAddress takeDefaultAddressForPatient(
            int patientId) {


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


  
    private void ensureRequiredFields(
            PatientAddress address) {

        if (address.takeState() == null ||
                address.takeState().isBlank()) {

            throw new IllegalArgumentException(
                    "State is required"
            );
        }

        if (address.takeDistrict() == null ||
                address.takeDistrict().isBlank()) {

            throw new IllegalArgumentException(
                    "District is required"
            );
        }

        if (address.takePincode() == null ||
                address.takePincode().isBlank()) {

            throw new IllegalArgumentException(
                    "Pincode is required"
            );
        }

        if (address.takePatientId() <= 0) {

            throw new IllegalArgumentException(
                    "Valid patient ID is required"
            );
        }
    }


    

    private void ensureUniqueAddressTypeForAdd(
            PatientAddress address) {

        if (patientAddressRepository
                .existsByPatientIdAndAddressType(
                        address.takePatientId(),
                        address.takeAddressType()
                )) {

            throw new IllegalArgumentException(
                    "Patient already has an address of type: "
                            + address.takeAddressType()
            );
        }
    }


  

    private void ensureUniqueAddressTypeForUpdate(
            PatientAddress address) {

        if (patientAddressRepository
                .existsByPatientIdAndAddressTypeAndAddressIdNot(
                        address.takePatientId(),
                        address.takeAddressType(),
                        address.takeAddressId()
                )) {

            throw new IllegalArgumentException(
                    "Patient already has an address of type: "
                            + address.takeAddressType()
            );
        }
    }    
}
