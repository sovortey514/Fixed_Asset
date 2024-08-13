package com.twd.SpringSecurityJWT.service.impl;

import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import com.twd.SpringSecurityJWT.repository.FixedAssetCountsRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetCountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FixedAssetCountsServiceImpl implements FixedAssetCountsService {

    @Autowired
    private FixedAssetCountsRepository fixedAssetCountsRepository;

    @Override
    public FixedAssetCounts createFixedAssetCount(FixedAssetCounts fixedAssetCounts) {
        fixedAssetCounts.setCreatedAt(LocalDateTime.now());
        fixedAssetCounts.setUpdatedAt(LocalDateTime.now());
        return fixedAssetCountsRepository.save(fixedAssetCounts);
    }

    @Override
    public void deleteFixedAssetCount(Long id) {
        fixedAssetCountsRepository.deleteById(id);
    }

    @Override
    public List<FixedAssetCounts> getAllFixedAssetCounts() {
        return fixedAssetCountsRepository.findAll();
    }

    @Override
    public Optional<FixedAssetCounts> getFixedAssetCountById(Long id) {
        return fixedAssetCountsRepository.findById(id);
    }

    @Override
    public FixedAssetCounts updateFixedAssetCount(Long id, FixedAssetCounts fixedAssetCounts) {
        Optional<FixedAssetCounts> existingFixedAssetCounts = fixedAssetCountsRepository.findById(id);
        if (existingFixedAssetCounts.isPresent()) {
            FixedAssetCounts updatedFixedAssetCounts = existingFixedAssetCounts.get();
            updatedFixedAssetCounts.setCountDate(fixedAssetCounts.getCountDate());
            updatedFixedAssetCounts.setQuantityCounted(fixedAssetCounts.getQuantityCounted());
            updatedFixedAssetCounts.setExistenceAsset(fixedAssetCounts.getExistenceAsset());
            updatedFixedAssetCounts.setCondition(fixedAssetCounts.getCondition());
            updatedFixedAssetCounts.setBuilding(fixedAssetCounts.getBuilding());
            updatedFixedAssetCounts.setDepartment(fixedAssetCounts.getDepartment());
            updatedFixedAssetCounts.setRemarks(fixedAssetCounts.getRemarks());
            updatedFixedAssetCounts.setUpdatedAt(LocalDateTime.now());
            
            return fixedAssetCountsRepository.save(updatedFixedAssetCounts);
        } else {
            throw new IllegalArgumentException("FixedAssetCount not found");
        }
    }
}
