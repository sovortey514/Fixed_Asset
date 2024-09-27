package com.twd.SpringSecurityJWT.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.SpringSecurityJWT.entity.FileData;
import com.twd.SpringSecurityJWT.entity.FixedAsset;


@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long>{

	Optional<FileData> findByName(String fileName);

    // Optional<FileData> findByFixedAsset(FixedAsset fixedAsset);

    
    List<FileData> findByFixedAsset(FixedAsset fixedAsset);

}