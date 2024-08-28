package com.twd.SpringSecurityJWT.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.repository.CategoryRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetService;

@Service
public class FixedAssetServiceImpl implements FixedAssetService {

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public FixedAsset createFixedAsset(FixedAsset fixedAsset, Integer categoryId) {
        
        validateFixedAsset(fixedAsset);

         // Check if a fixed asset with the same serial number already exists
         if (fixedAssetRepository.existsBySerialNumber(fixedAsset.getSerialNumber())) {
            throw new RuntimeException("A fixed asset with the same serial number already exists.");
        }

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            fixedAsset.setCategory(categoryOpt.get());
            return fixedAssetRepository.save(fixedAsset);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    private void validateFixedAsset(FixedAsset fixedAsset) {
        if (fixedAsset.getName() == null || fixedAsset.getName().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (fixedAsset.getModel() == null || fixedAsset.getModel().isEmpty()) {
            throw new RuntimeException("Model is required");
        }
        if (fixedAsset.getYear() == null) {
            throw new RuntimeException("Year is required");
        }
        if (fixedAsset.getPrice() == null) {
            throw new RuntimeException("Price is required");
        }
        if (fixedAsset.getSerialNumber() == null || fixedAsset.getSerialNumber().isEmpty()) {
            throw new RuntimeException("Serial number is required");
        }
        if (fixedAsset.getPurchaseDate() == null) {
            throw new RuntimeException("Purchase date is required");
        }
        if (fixedAsset.getUnit() == null || fixedAsset.getUnit().isEmpty()) {
            throw new RuntimeException("Unit is required");
        }
        if (fixedAsset.getQuantity() == null) {
            throw new RuntimeException("Quantity is required");
        }
        if (fixedAsset.getStatustext() == null) {
            throw new RuntimeException("Statustext is required");
        }

        
        
     
    }

    //

    @Override
    public void deleteFixedAsset(Long id) {
        // Implement deletion logic
    }

    @Override
    public List<FixedAsset> getAllFixedAssets() {
        return fixedAssetRepository.findAllWithCategory();
    }

    
    @Override
    public Optional<FixedAsset> getFixedAssetById(Long id) {
       
        return fixedAssetRepository.findById(id);
    }

    @Override
    public FixedAsset updateFixedAsset(Long id, FixedAsset fixedAsset) {
        // Implement update logic

        return fixedAssetRepository.findById(id).map(existingAsset -> {
            existingAsset.setName(fixedAsset.getName());
            existingAsset.setModel(fixedAsset.getModel());
            existingAsset.setYear(fixedAsset.getYear());
            existingAsset.setPrice(fixedAsset.getPrice());
            existingAsset.setSerialNumber(fixedAsset.getSerialNumber());
            existingAsset.setPurchaseDate(fixedAsset.getPurchaseDate());
            existingAsset.setUnit(fixedAsset.getUnit());
            existingAsset.setQuantity(fixedAsset.getQuantity());
            existingAsset.setImage(fixedAsset.getImage());
            existingAsset.setCategory(fixedAsset.getCategory());
            existingAsset.setStatus(fixedAsset.getStatus());
            existingAsset.setStatustext(fixedAsset.getStatustext());
            existingAsset.setAssetHolder(fixedAsset.getAssetHolder());
            // existingAsset.setStatustext(fixedAsset.getStatustext());

            return fixedAssetRepository.save(existingAsset);
        }).orElseThrow(() -> new RuntimeException("Fixed Asset not found"));
    }

    @Override
    public List<FixedAsset> getAllFixedAssetsWithDepartment(Long departmentId) {
        return fixedAssetRepository.findFixedAssetsByDepartmentId(departmentId);
    }



    // @Override
    // public List<Object[]> getAssetsCountInEachDepartment() {
    //     return fixedAssetRepository.countAssetsInEachDepartment();
    // }
}