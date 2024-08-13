package com.twd.SpringSecurityJWT.service;

import java.util.List;
import java.util.Optional;

import com.twd.SpringSecurityJWT.entity.FixedAsset;

public interface FixedAssetService {

    FixedAsset createFixedAsset(FixedAsset fixedAsset, Integer categoryId);
    
    void deleteFixedAsset(Long id);

    List<FixedAsset> getAllFixedAssets();

    Optional<FixedAsset> getFixedAssetById(Long id);

    FixedAsset updateFixedAsset(Long id, FixedAsset fixedAsset);
}
