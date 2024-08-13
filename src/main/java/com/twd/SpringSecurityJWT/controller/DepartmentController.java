package com.twd.SpringSecurityJWT.controller;

import com.twd.SpringSecurityJWT.entity.Department;
import com.twd.SpringSecurityJWT.service.DepartmentService;
import com.twd.SpringSecurityJWT.dto.ReqRes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/createDepartment")
    public ResponseEntity<ReqRes> createDepartment(@RequestBody Department department) {
        ReqRes resp = new ReqRes();
        try {
            Department createdDepartment = departmentService.creaDepartment(department);
            resp.setDepartment(createdDepartment);
            resp.setMessage("Department Created Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(400);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }


    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<ReqRes> deleteDepartment(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            departmentService.deleteDepartment(id);
            resp.setMessage("Department Deleted Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(404);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getAllDepartments")
    public ResponseEntity<ReqRes> getAllDepartments() {
        ReqRes resp = new ReqRes();
        try {
            List<Department> departments = departmentService.getAllDepartments();
            resp.setDepartments(departments);
            resp.setMessage("Departments Retrieved Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getDepartmentById/{id}")
    public ResponseEntity<ReqRes> getDepartmentById(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            Optional<Department> department = departmentService.getDepartmentById(id);
            if (department.isPresent()) {
                resp.setDepartment(department.get());
                resp.setMessage("Department Retrieved Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Department Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<ReqRes> updateDepartment(@PathVariable Long id,@RequestBody Department department) {
        ReqRes resp = new ReqRes();
        try {
            Department updatedDepartment = departmentService.updateDepartment(id, department);
            if (updatedDepartment != null) {
                resp.setDepartment(updatedDepartment);
                resp.setMessage("Department Updated Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Department Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
