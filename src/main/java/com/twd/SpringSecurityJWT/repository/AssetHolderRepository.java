package com.twd.SpringSecurityJWT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.SpringSecurityJWT.entity.AssetHolder;

@Repository
public interface AssetHolderRepository extends JpaRepository<AssetHolder, Long> {
}

