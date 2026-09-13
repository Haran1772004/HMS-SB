package com.hospital.hospital_spring.service;

import com.hospital.hospital_spring.entity.Department;
import com.hospital.hospital_spring.exception.DepartmentNotFoundException;
import com.hospital.hospital_spring.repository.DepartmentRepository;
import com.hospital.hospital_spring.model.AccountStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void addDepartment(Department department) {

        if (department == null) {
            throw new IllegalArgumentException("Department is required");
        }

        if (department.takeName() == null ||
                department.takeName().isBlank()) {

            throw new IllegalArgumentException("Department name is required");
        }

        ensureUniqueForAdd(department);
        if (department.takeStatus() == null) {
            department.setStatus(AccountStatus.ACTIVE);
        }

        departmentRepository.save(department);
    }

    public void updateDepartment(Department department) {

        if (department == null) {
            throw new IllegalArgumentException("Department is required");
        }

        Department existingDepartment =
                departmentRepository.findById(
                        department.takeDepartmentId()
                ).orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with ID: "
                                        + department.takeDepartmentId()
                        )
                );

        if (department.takeName() == null ||
                department.takeName().isBlank()) {

            throw new IllegalArgumentException("Department name is required");
        }

        ensureUniqueForUpdate(department);

        existingDepartment.setName(
                department.takeName()
        );

        existingDepartment.setDescription(
                department.takeDescription()
        );

        departmentRepository.save(existingDepartment);
    }

    public void deactivateDepartment(int departmentId) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new DepartmentNotFoundException(
                                        "Department not found with ID: "
                                                + departmentId
                                )
                        );

        department.setStatus(AccountStatus.INACTIVE);

        departmentRepository.save(department);
    }

    public void activateDepartment(int departmentId) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new DepartmentNotFoundException(
                                        "Department not found with ID: "
                                                + departmentId
                                )
                        );

        department.setStatus(AccountStatus.ACTIVE);

        departmentRepository.save(department);
    }

    public Department takeDepartmentById(int departmentId) {

        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with ID: "
                                        + departmentId
                        )
                );
    }

    public List<Department> takeAllDepartments() {

        return departmentRepository.findAll();
    }

    private void ensureUniqueForAdd(Department department) {

        if (departmentRepository.existsByName(
                department.takeName())) {

            throw new IllegalArgumentException(
                    "Department with name already exists: "
                            + department.takeName()
            );
        }
    }

    private void ensureUniqueForUpdate(Department department) {

        if (departmentRepository.existsByNameAndDepartmentIdNot(
                department.takeName(),
                department.takeDepartmentId())) {

            throw new IllegalArgumentException(
                    "Department with name already exists: "
                            + department.takeName()
            );
        }
    }
}