package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.FixedAsset;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, Long> {
    
    @Query("SELECT fa FROM FixedAsset fa JOIN FETCH fa.category where fa.status = '1'") 
    List<FixedAsset> findAllWithCategory();
}
