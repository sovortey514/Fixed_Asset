package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.Building;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByName(String name);
}