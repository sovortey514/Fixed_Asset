package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;

import java.util.List;
import java.util.Optional;

public interface FixedAssetCountsService {
    FixedAssetCounts createFixedAssetCount(FixedAssetCounts fixedAssetCounts);
    void deleteFixedAssetCount(Long id);
    List<FixedAssetCounts> getAllFixedAssetCounts();
    Optional<FixedAssetCounts> getFixedAssetCountById(Long id);
    FixedAssetCounts updateFixedAssetCount(Long id, FixedAssetCounts fixedAssetCounts);
}
