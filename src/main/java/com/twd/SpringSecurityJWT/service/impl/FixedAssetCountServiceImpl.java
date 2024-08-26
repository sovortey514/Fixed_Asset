package com.twd.SpringSecurityJWT.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT.entity.AssetHolder;
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
    // Log incoming request
    System.out.println("Received request: " + fixedAssetCounts);

    // Validate and set required fields
    if (fixedAssetCounts.getCreatedBy() == null || fixedAssetCounts.getCreatedBy().isEmpty()) {
        throw new IllegalArgumentException("Created By must be provided");
    }

    fixedAssetCounts.setCreatedAt(LocalDateTime.now());
    fixedAssetCounts.setUpdatedAt(LocalDateTime.now());
    fixedAssetCounts.setUpdatedBy(fixedAssetCounts.getCreatedBy()); // Set updatedBy to the same as createdBy initially

    // Ensure the Department is set
    Department department = findDepartmentById(fixedAssetCounts.getDepartment().getId());
    fixedAssetCounts.setDepartment(department);

    // Persist FixedAssetCounts first
    FixedAssetCounts savedFixedAssetCounts = fixedAssetCountsRepository.save(fixedAssetCounts);

    // Handle FixedAssetDetail entities
    for (FixedAssetDetail detail : savedFixedAssetCounts.getAssetDetails()) {
        // Log details to check if fields are set
        System.out.println("Before save: " + detail);

        // Set the reference to the saved FixedAssetCounts
        detail.setFixedAssetCount(savedFixedAssetCounts);

        // Validate detail fields
        if (detail.getQuantityCounted() == null) {
            throw new IllegalArgumentException("Quantity Counted must be provided");
        }
        if (detail.getConditions() == null || detail.getConditions().isEmpty()) {
            throw new IllegalArgumentException("Conditions must be provided");
        }
        if (detail.getExistenceAsset() == null || detail.getExistenceAsset().isEmpty()) {
            throw new IllegalArgumentException("Existence Asset must be provided");
        }

        // Ensure AssetHolder and FixedAsset are managed
        if (detail.getAssetHolder() != null) {
            AssetHolder assetHolder = findAssetHolderById(detail.getAssetHolder().getId());
            detail.setAssetHolder(assetHolder);
            System.out.println("AssetHolder details: " + assetHolder);
        }
        if (detail.getFixedAsset() != null) {
            FixedAsset fixedAsset = findFixedAssetById(detail.getFixedAsset().getId());
            detail.setFixedAsset(fixedAsset);
            System.out.println("FixedAsset details: " + fixedAsset);
        }

        // Save FixedAssetDetail entities
        fixedAssetDetailRepository.save(detail);

        // Log after save
        System.out.println("After save: " + detail);
    }

    return savedFixedAssetCounts;
}
    
    private Department findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
    }
    
    private AssetHolder findAssetHolderById(Long assetHolderId) {
        return assetHolderRepository.findById(assetHolderId)
                .orElseThrow(() -> new RuntimeException("AssetHolder not found with ID: " + assetHolderId));
    }
    
    private FixedAsset findFixedAssetById(Long assetId) {
        return fixedAssetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("FixedAsset not found with ID: " + assetId));
    }
    

    @Override
    public void deleteFixedAssetCount(Long id) {
        // Implementation here
    }

    @Override
    public List<FixedAssetCounts> getAllFixedAssetCounts() {
        // Implementation here
        return fixedAssetCountsRepository.findAll();
    }

    @Override
    public Optional<FixedAssetCounts> getAllFixedAssetCountsById(Long id) {
        // Implementation here
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
}
