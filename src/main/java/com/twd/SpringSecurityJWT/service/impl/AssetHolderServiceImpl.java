package com.twd.SpringSecurityJWT.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twd.SpringSecurityJWT.entity.AssetHolder;

import com.twd.SpringSecurityJWT.repository.AssetHolderRepository;
import com.twd.SpringSecurityJWT.repository.DepartmentRepository;
import com.twd.SpringSecurityJWT.service.AssetHolderService;


@Service
public class AssetHolderServiceImpl implements AssetHolderService {

    @Autowired
    private AssetHolderRepository assetHolderRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public AssetHolder createAssetHolder(AssetHolder assetHolder) {

        validateNameEmailAndPhoneNumber(assetHolder);

        if (assetHolder.getDepartment() == null || !departmentRepository.existsById(assetHolder.getDepartment().getId())) {
            throw new IllegalArgumentException("Invalid department ID");
        }

        assetHolder.setCreatedAt(LocalDateTime.now());
        assetHolder.setUpdatedAt(LocalDateTime.now());
        return assetHolderRepository.save(assetHolder);
    }

    @Override
    public void deleteAssetHolder(Long id) {
        if (assetHolderRepository.existsById(id)) {
            assetHolderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("AssetHolder with ID " + id + " does not exist.");
        }
    }
    
    @Override
    public List<AssetHolder> getAllAssetHolders() {
        return assetHolderRepository.findAll();
    }

    @Override
    public Optional<AssetHolder> getAssetHolderById(Long id) {
        return assetHolderRepository.findById(id);
    }

    @Override
    public AssetHolder updateAssetHolder(Long id, AssetHolder assetHolder) {
        Optional<AssetHolder> existingAssetHolderOpt = assetHolderRepository.findById(id);
        if (existingAssetHolderOpt.isPresent()) {
            AssetHolder existingAssetHolder = existingAssetHolderOpt.get();

            updateExistingAssetHolder(existingAssetHolder, assetHolder);

            return assetHolderRepository.save(existingAssetHolder);
        } else {
            throw new IllegalArgumentException("AssetHolder not found with ID " + id);
        }
    }

    private void updateExistingAssetHolder(AssetHolder existingAssetHolder, AssetHolder assetHolder) {
        existingAssetHolder.setName(assetHolder.getName());
        existingAssetHolder.setEmail(assetHolder.getEmail());
        existingAssetHolder.setPhoneNumber(assetHolder.getPhoneNumber());
        existingAssetHolder.setDepartment(assetHolder.getDepartment());
        existingAssetHolder.setGivenDate(assetHolder.getGivenDate());
        existingAssetHolder.setReturnDate(assetHolder.getReturnDate());
        existingAssetHolder.setReturnNote(assetHolder.getReturnNote());
        existingAssetHolder.setRemark(assetHolder.getRemark());
        existingAssetHolder.setAssignedAt(assetHolder.getAssignedAt());
        existingAssetHolder.setUpdatedAt(LocalDateTime.now());
    }


    private void validateNameEmailAndPhoneNumber(AssetHolder assetHolder) {
        if (assetHolder.getName() == null || assetHolder.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("AssetHolder name must not be empty");
        }
        if (assetHolder.getEmail() == null || assetHolder.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (!assetHolder.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (assetHolder.getPhoneNumber() == null || assetHolder.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number must not be empty");
        }
        if (!assetHolder.getPhoneNumber().matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}

