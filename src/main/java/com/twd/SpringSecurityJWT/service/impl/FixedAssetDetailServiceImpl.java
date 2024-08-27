package com.twd.SpringSecurityJWT.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT.entity.AssetHolder;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.entity.FixedAssetDetail;
import com.twd.SpringSecurityJWT.repository.AssetHolderRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetDetailRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetDetailService;

@Service
public class FixedAssetDetailServiceImpl implements FixedAssetDetailService{

    @Autowired
    private AssetHolderRepository assetHolderRepository;

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    @Autowired
    private FixedAssetDetailRepository fixedAssetDetailRepository;

    @Override
    public FixedAssetDetail createFixedAssetDetail(FixedAssetDetail fixedAssetDetail) {

        if (fixedAssetDetail.getAssetHolder() == null ||
        !assetHolderRepository.existsById(fixedAssetDetail.getAssetHolder().getId())) {
        throw new IllegalArgumentException("Invalid asset holder ID");
        }

        if (fixedAssetDetail.getFixedAsset() == null ||
        !fixedAssetRepository.existsById(fixedAssetDetail.getFixedAsset().getId())) {
        throw new IllegalArgumentException("Invalid fixed asset ID");
        }
            AssetHolder managedAssetHolder = assetHolderRepository.findById(fixedAssetDetail.getAssetHolder().getId())
            .orElseThrow(() -> new IllegalArgumentException("Asset holder not found"));
        FixedAsset managedFixedAsset = fixedAssetRepository.findById(fixedAssetDetail.getFixedAsset().getId())
            .orElseThrow(() -> new IllegalArgumentException("Fixed asset not found"));


        fixedAssetDetail.setConditions(fixedAssetDetail.getConditions());
        fixedAssetDetail.setExistenceAsset(fixedAssetDetail.getExistenceAsset());
        fixedAssetDetail.setRemarks(fixedAssetDetail.getRemarks());
        fixedAssetDetail.setQuantityCounted(fixedAssetDetail.getQuantityCounted());
        fixedAssetDetail.setAssetHolder(fixedAssetDetail.getAssetHolder());
        fixedAssetDetail.setFixedAsset(fixedAssetDetail.getFixedAsset());
        return fixedAssetDetailRepository.save(fixedAssetDetail);
    }

    @Override
    public void deleteFixedAssetDetail(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<FixedAssetDetail> getAllFixedAssetDetails() {
        
        return fixedAssetDetailRepository.findAll();
    }

    @Override
    public Optional<FixedAssetDetail> getFixedAssetDetailById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public FixedAssetDetail updateFixedAssetDetail(Long id, FixedAssetDetail fixedAssetDetail) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
