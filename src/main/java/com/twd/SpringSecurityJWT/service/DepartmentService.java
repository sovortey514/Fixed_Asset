package com.twd.SpringSecurityJWT.service;


import com.twd.SpringSecurityJWT.entity.Department;
import java.util.List;
import java.util.Optional;


public interface DepartmentService {
    
    Department creaDepartment(Department department);
    void deleteDepartment(Long id);
    List<Department> getAllDepartments();
    Optional<Department> getDepartmentById(Long id);
    Department updateDepartment(Long id, Department department);
}
