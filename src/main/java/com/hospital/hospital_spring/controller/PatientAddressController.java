package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.PatientAddress;
import com.hospital.hospital_spring.service.PatientAddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient-addresses")
public class PatientAddressController {

    private final PatientAddressService patientAddressService;

    public PatientAddressController(
            PatientAddressService patientAddressService) {

        this.patientAddressService = patientAddressService;
    }



    @PostMapping
    public void addAddress(
            @RequestBody PatientAddress address) {

        patientAddressService.addAddress(address);
    }


   

    @PutMapping("/{addressId}")
    public void updateAddress(
            @PathVariable int addressId,
            @RequestBody PatientAddress address) {

        address.setAddressId(addressId);

        patientAddressService.updateAddress(address);
    }


    

    @DeleteMapping("/{addressId}")
    public void removeAddress(
            @PathVariable int addressId) {

        patientAddressService.removeAddress(addressId);
    }



    @GetMapping("/{addressId}")
    public PatientAddress getAddressById(
            @PathVariable int addressId) {

        return patientAddressService
                .takeAddressById(addressId);
    }


    

    @GetMapping("/patient/{patientId}")
    public List<PatientAddress> getAddressesByPatient(
            @PathVariable int patientId) {

        return patientAddressService
                .takeAddressesByPatient(patientId);
    }


    

    @GetMapping("/patient/{patientId}/default")
    public PatientAddress getDefaultAddress(
            @PathVariable int patientId) {

        return patientAddressService
                .takeDefaultAddressForPatient(patientId);
    }


   
    @GetMapping
    public List<PatientAddress> getAllAddresses() {

        return patientAddressService
                .takeAllAddresses();
    }
}