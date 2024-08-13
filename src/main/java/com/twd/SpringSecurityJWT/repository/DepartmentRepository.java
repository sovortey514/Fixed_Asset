package com.twd.SpringSecurityJWT.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.twd.SpringSecurityJWT.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

//     @Query("SELECT fa FROM department fa JOIN FETCH fa.building")
        Optional<Department> findByName(String name);
 }