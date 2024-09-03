package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.FixedAsset;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, Long> {
    
 
    @Query("SELECT DISTINCT fa FROM FixedAsset fa " +
       "JOIN FETCH fa.category " +
       "LEFT JOIN FETCH fa.assetHolder " +
       "WHERE fa.status = '1'")
    List<FixedAsset> findAllWithCategory();

    @Query("SELECT fa FROM FixedAsset fa " +
           "JOIN fa.assetHolder ah " +
           "JOIN ah.department d " +
           "WHERE d.id = :departmentId")
    List<FixedAsset> findFixedAssetsByDepartmentId(@Param("departmentId") Long departmentId);
    
    boolean existsBySerialNumber(String serialNumber);
    
}
