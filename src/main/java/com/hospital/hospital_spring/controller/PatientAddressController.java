package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Patient;
import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.exception.AuthorizationException;
import com.hospital.hospital_spring.repository.PatientAddressRepository;
import com.hospital.hospital_spring.repository.PatientRepository;
import com.hospital.hospital_spring.security.CurrentUser;
import com.hospital.hospital_spring.service.PatientAddressService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient-addresses")
public class PatientAddressController {

    private final PatientAddressService patientAddressService;
    private final PatientRepository patientRepository;
    private final PatientAddressRepository patientAddressRepository;

    public PatientAddressController(
            PatientAddressService patientAddressService,
            PatientRepository patientRepository,
            PatientAddressRepository patientAddressRepository) {

        this.patientAddressService = patientAddressService;
        this.patientRepository = patientRepository;
        this.patientAddressRepository = patientAddressRepository;
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @PostMapping
    public void addAddress(
            @RequestBody PatientAddress address) {

        if (isPatientRole()) {
            ensureOwnPatientByPatientId(address.takePatientId());
        }

        patientAddressService.addAddress(address);
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @PutMapping("/{addressId}")
    public void updateAddress(
            @PathVariable int addressId,
            @RequestBody PatientAddress address) {

        address.setAddressId(addressId);

        if (isPatientRole()) {
            ensureOwnAddress(addressId);
        }

        patientAddressService.updateAddress(address);
    }

   
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @DeleteMapping("/{addressId}")
    public void removeAddress(
            @PathVariable int addressId) {

        if (isPatientRole()) {
            ensureOwnAddress(addressId);
        }

        patientAddressService.removeAddress(addressId);
    }

     
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me")
    public List<PatientAddress> getMyAddresses() {
        Patient ownPatient = getAuthenticatedPatient();
        return patientAddressService.takeAddressesByPatient(ownPatient.takePatientId());
    }

    
    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/me/default")
    public PatientAddress getMyDefaultAddress() {
        Patient ownPatient = getAuthenticatedPatient();
        return patientAddressService.takeDefaultAddressForPatient(ownPatient.takePatientId());
    }

     
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @GetMapping("/{addressId}")
    public PatientAddress getAddressById(
            @PathVariable int addressId) {

        if (isPatientRole()) {
            ensureOwnAddress(addressId);
        }

        return patientAddressService.takeAddressById(addressId);
    }

  
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @GetMapping("/patient/{patientId}")
    public List<PatientAddress> getAddressesByPatient(
            @PathVariable int patientId) {

        if (isPatientRole()) {
            ensureOwnPatientByPatientId(patientId);
        }

        return patientAddressService.takeAddressesByPatient(patientId);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    @GetMapping("/patient/{patientId}/default")
    public PatientAddress getDefaultAddress(
            @PathVariable int patientId) {

        if (isPatientRole()) {
            ensureOwnPatientByPatientId(patientId);
        }

        return patientAddressService.takeDefaultAddressForPatient(patientId);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping
    public List<PatientAddress> getAllAddresses() {

        return patientAddressService.takeAllAddresses();
    }

    private boolean isPatientRole() {
        return "ROLE_PATIENT".equals(CurrentUser.getRole());
    }

    private Patient getAuthenticatedPatient() {
        int currentUserId = CurrentUser.getUserId();
        return patientRepository
                .findByUserId(currentUserId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "No patient profile linked to your account."
                        )
                );
    }

    private void ensureOwnPatientByPatientId(int requestedPatientId) {
        Patient ownPatient = getAuthenticatedPatient();
        if (ownPatient.takePatientId() != requestedPatientId) {
            throw new AuthorizationException(
                    "Access denied: you can only manage your own addresses."
            );
        }
    }

    private void ensureOwnAddress(int addressId) {
        PatientAddress address = patientAddressRepository
                .findById(addressId)
                .orElseThrow(() ->
                        new AuthorizationException(
                                "Address not found or access denied."
                        )
                );

        ensureOwnPatientByPatientId(address.takePatientId());
    }
}
