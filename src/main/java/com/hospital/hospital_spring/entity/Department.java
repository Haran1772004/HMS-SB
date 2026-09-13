package com.hospital.hospital_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hospital.hospital_spring.model.AccountStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "departments")
@JsonPropertyOrder({
        "departmentId",
        "name",
        "description",
        "status",
        "statusName"
})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    public Department() {
    }

    public Department(
            int departmentId,
            String name,
            String description,
            AccountStatus status) {

        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Department(
            int departmentId,
            String name,
            String description,
            String status) {

        this(   departmentId,
                name,
                description,
                status == null
                        ? null
                        : AccountStatus.valueOf(status.toUpperCase())
        );
    }

    @JsonProperty("departmentId")
    public int takeDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @JsonProperty("name")
    public String takeName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String takeDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("status")
    public AccountStatus takeStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = AccountStatus.valueOf(status.toUpperCase());
    }

    @JsonProperty("statusName")
    public String takeStatusName() {
        return status == null ? null : status.name();
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}