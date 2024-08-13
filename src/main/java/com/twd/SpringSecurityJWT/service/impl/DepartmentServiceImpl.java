package com.twd.SpringSecurityJWT.service.impl;

import com.twd.SpringSecurityJWT.entity.Building;
import com.twd.SpringSecurityJWT.entity.Department;
import com.twd.SpringSecurityJWT.repository.DepartmentRepository;
import com.twd.SpringSecurityJWT.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

 
    @Override
    public Department creaDepartment(Department department) {
        validateDepartment(department);
        
        Optional<Department> existingBuilding = departmentRepository.findByName(department.getName());
        if (existingBuilding.isPresent()) {
            throw new IllegalArgumentException("Building with this name already exists");
        }
        
        department.setName(department.getName());
        department.setBuilding(department.getBuilding());
        department.setFloorNumber(department.getFloorNumber());
        department.setDescription(department.getDescription());
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department updateDepartment(Long id, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setName(department.getName());
            updatedDepartment.setDescription(department.getDescription());
            updatedDepartment.setFloorNumber(department.getFloorNumber());
            updatedDepartment.setBuilding(department.getBuilding());
            updatedDepartment.setUpdatedAt(LocalDateTime.now());
            validateDepartment(updatedDepartment);
            return departmentRepository.save(updatedDepartment);
        } else {
            throw new IllegalArgumentException("Department not found");
        }
    }

   private void validateDepartment(Department department) {
        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name must not be empty");
        }
    }


}
