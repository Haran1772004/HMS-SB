package com.hospital.hospital_spring.controller;

import com.hospital.hospital_spring.entity.Department;
import com.hospital.hospital_spring.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @PostMapping
    public void addDepartment(
            @RequestBody Department department) {

        departmentService.addDepartment(department);
    }

    @PutMapping("/{departmentId}")
    public void updateDepartment(
            @PathVariable int departmentId,
            @RequestBody Department department) {

        department.setDepartmentId(departmentId);

        departmentService.updateDepartment(department);
    }

    @PutMapping("/{departmentId}/deactivate")
    public void deactivateDepartment(
            @PathVariable int departmentId) {

        departmentService.deactivateDepartment(departmentId);
    }

    // ACTIVATE
    // ==========================================

    @PutMapping("/{departmentId}/activate")
    public void activateDepartment(
            @PathVariable int departmentId) {

        departmentService.activateDepartment(departmentId);
    }


    @GetMapping("/{departmentId}")
    public Department getDepartmentById(
            @PathVariable int departmentId) {

        return departmentService.takeDepartmentById(departmentId);
    }


    @GetMapping
    public List<Department> getAllDepartments() {

        return departmentService.takeAllDepartments();
    }
}