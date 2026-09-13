package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hospital.hospital_spring.model.AddressType;
import jakarta.persistence.*;

@Entity
@Table(
        name = "patient_addresses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_patient_address_type",
                        columnNames = {"patient_id", "address_type"}
                )
        }
)
@JsonPropertyOrder({
        "addressId",
        "patientId",
        "state",
        "district",
        "pincode",
        "addressType"
})
public class PatientAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    @Column(name = "patient_id", nullable = false)
    private int patientId;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "pincode", nullable = false)
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType addressType;

    public PatientAddress() {
    }

    public PatientAddress(
            int addressId,
            int patientId,
            String state,
            String district,
            String pincode,
            AddressType addressType) {

        this.addressId = addressId;
        this.patientId = patientId;
        this.state = state;
        this.district = district;
        this.pincode = pincode;
        this.addressType = addressType;
    }

    public PatientAddress(
            int addressId,
            int patientId,
            String state,
            String district,
            String pincode,
            String addressType) {

        this(
                addressId,
                patientId,
                state,
                district,
                pincode,
                addressType == null
                        ? null
                        : AddressType.valueOf(addressType.toUpperCase())
        );
    }

    @JsonProperty("addressId")
    public int takeAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @JsonProperty("patientId")
    public int takePatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @JsonProperty("state")
    public String takeState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("district")
    public String takeDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


    @JsonProperty("pincode")
    public String takePincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


    @JsonProperty("addressType")
    public AddressType takeAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public void setAddressType(String addressType) {

        this.addressType = addressType == null
                ? null
                : AddressType.valueOf(addressType.toUpperCase());
    }
  @Override
    public String toString() {

        return "PatientAddress{" +
                "addressId=" + addressId +
                ", patientId=" + patientId +
                ", state='" + state + '\'' +
                ", district='" + district + '\'' +
                ", pincode='" + pincode + '\'' +
                ", addressType=" + addressType +
                '}';
    }
}