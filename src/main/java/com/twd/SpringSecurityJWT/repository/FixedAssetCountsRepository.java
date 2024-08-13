package com.twd.SpringSecurityJWT.repository;

import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedAssetCountsRepository extends JpaRepository<FixedAssetCounts, Long> {
}
