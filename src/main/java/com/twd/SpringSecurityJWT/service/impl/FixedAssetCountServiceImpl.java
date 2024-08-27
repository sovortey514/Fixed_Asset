package com.twd.SpringSecurityJWT.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT.entity.AssetHolder;
import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.entity.Department;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import com.twd.SpringSecurityJWT.entity.FixedAssetDetail;
import com.twd.SpringSecurityJWT.repository.AssetHolderRepository;
import com.twd.SpringSecurityJWT.repository.DepartmentRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetCountsRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetDetailRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetCountService;
import java.time.LocalDateTime;

@Service
public class FixedAssetCountServiceImpl implements FixedAssetCountService {

    @Autowired
    private FixedAssetCountsRepository fixedAssetCountsRepository;

    @Autowired
    private FixedAssetDetailRepository fixedAssetDetailRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    @Autowired
    private AssetHolderRepository assetHolderRepository;

    @Override
    public FixedAssetCounts createFixedAssetCounts(FixedAssetCounts fixedAssetCounts) {
     
        if (fixedAssetCounts.getDepartment() == null || !departmentRepository.existsById(fixedAssetCounts.getDepartment().getId())) {
            throw new IllegalArgumentException("Invalid department ID");
        }
        
        if (fixedAssetCounts.getCreatedBy() == null || fixedAssetCounts.getCreatedBy().isEmpty()) {
            throw new IllegalArgumentException("Created By must be provided");
        }
        fixedAssetCounts.setCreatedAt(LocalDateTime.now());
        fixedAssetCounts.setUpdatedAt(LocalDateTime.now());
        fixedAssetCounts.setUpdatedBy(fixedAssetCounts.getCreatedBy());
        return fixedAssetCountsRepository.save(fixedAssetCounts);
    }
    
    

    // @Override
    // public void deleteFixedAssetCount(Long id) {
    //     // Implementation here
    // }

    @Override
    public List<FixedAssetCounts> getAllFixedAssetCounts() {
        return fixedAssetCountsRepository.findAll();
    }
    @Override
    public Optional<FixedAssetCounts> getAllFixedAssetCountsById(Long id) {
        return fixedAssetCountsRepository.findById(id);
    }

    @Override
    public FixedAssetCounts upFixedAssetCounts(Long id, FixedAssetCounts fixedAssetCounts) {
        // Implementation here
        return fixedAssetCountsRepository.findById(id)
                .map(existing -> {
                    existing.setUpdatedAt(LocalDateTime.now());
                    existing.setUpdatedBy(fixedAssetCounts.getUpdatedBy());
                    // Update other fields as necessary
                    return fixedAssetCountsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("FixedAssetCount not found with ID: " + id));
    }

    // @Override
    // public void deleteFixedAssetCount(Long id) {
    //     if (fixedAssetCountsRepository.existsById(id)) {
    //         fixedAssetCountsRepository.deleteById(id);
    //     } else {
    //         throw new IllegalArgumentException("AssetHolder with ID " + id + " does not exist.");
    //     }
    // }
 
}
