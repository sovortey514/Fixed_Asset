package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.entity.FixedAssetDetail;
import java.util.List;
import java.util.Optional;
public interface FixedAssetDetailService {
    List<FixedAssetDetail> getAllFixedAssetDetails();
    Optional<FixedAssetDetail> getFixedAssetDetailById(Long id);
    FixedAssetDetail createFixedAssetDetail(FixedAssetDetail fixedAssetDetail);
    FixedAssetDetail updateFixedAssetDetail(Long id, FixedAssetDetail fixedAssetDetail);
    void deleteFixedAssetDetail(Long id);
}
