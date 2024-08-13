package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
}
