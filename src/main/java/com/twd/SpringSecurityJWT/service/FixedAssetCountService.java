package com.twd.SpringSecurityJWT.service;


import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import java.util.List;
import java.util.Optional;

public interface FixedAssetCountService {
    
    FixedAssetCounts createFixedAssetCounts(FixedAssetCounts fixedAssetCounts);
    void deleteFixedAssetCount(Long id);
    List<FixedAssetCounts> getAllFixedAssetCounts();
    Optional<FixedAssetCounts> getAllFixedAssetCountsById(Long id);
    FixedAssetCounts upFixedAssetCounts(Long id, FixedAssetCounts fixedAssetCounts);

}
