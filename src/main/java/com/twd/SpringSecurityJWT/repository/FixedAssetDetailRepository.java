package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.FixedAssetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedAssetDetailRepository extends JpaRepository<FixedAssetDetail, Long> {
    
}
