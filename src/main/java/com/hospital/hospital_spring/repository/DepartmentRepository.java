package com.hospital.hospital_spring.repository;

import com.hospital.hospital_spring.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    boolean existsByName(String name);

    boolean existsByNameAndDepartmentIdNot(String name, int departmentId);
}