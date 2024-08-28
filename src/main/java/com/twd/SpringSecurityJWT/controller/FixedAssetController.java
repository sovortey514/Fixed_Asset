package com.twd.SpringSecurityJWT.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.SpringSecurityJWT.dto.FixedAssetRequest;
import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.AssetHolder;
import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.repository.AssetHolderRepository;
import com.twd.SpringSecurityJWT.repository.CategoryRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetService;

@RestController
@RequestMapping("/admin")
public class FixedAssetController {
    
    @Autowired
    private FixedAssetService fixedAssetService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AssetHolderRepository assetHolderRepository;

    @Autowired
    private FixedAssetRepository fixedAssetRepository;
    
    @PostMapping("/createFixedAsset")
    public ResponseEntity<ReqRes> createFixedAsset(@RequestBody FixedAssetRequest fixedAssetRequest) {
        ReqRes resp = new ReqRes();
        try {
            // Populate FixedAsset entity from the request
            FixedAsset fixedAssetToSave = new FixedAsset();
            fixedAssetToSave.setName(fixedAssetRequest.getName());
            fixedAssetToSave.setModel(fixedAssetRequest.getModel());
            fixedAssetToSave.setYear(fixedAssetRequest.getYear());
            fixedAssetToSave.setPrice(fixedAssetRequest.getPrice());
            fixedAssetToSave.setSerialNumber(fixedAssetRequest.getSerialNumber());
            fixedAssetToSave.setPurchaseDate(fixedAssetRequest.getPurchaseDate());
            fixedAssetToSave.setUnit(fixedAssetRequest.getUnit());
            fixedAssetToSave.setQuantity(fixedAssetRequest.getQuantity());
            fixedAssetToSave.setImage(fixedAssetRequest.getImage());
            fixedAssetToSave.setStatus(fixedAssetRequest.getStatus());
            fixedAssetToSave.setStatustext(fixedAssetRequest.getStatustext() != null ? fixedAssetRequest.getStatustext() : "Avaliable");

            Integer categoryId = fixedAssetRequest.getCategoryId();
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                fixedAssetToSave.setCategory(categoryOpt.get());
            } else {
                throw new RuntimeException("Category not found");
            }

            
            

            FixedAsset savedFixedAsset = fixedAssetService.createFixedAsset(fixedAssetToSave, categoryId);
            if (savedFixedAsset != null && savedFixedAsset.getId() != null) {
                resp.setFixedAsset(savedFixedAsset);
                resp.setMessage("Fixed Asset Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (RuntimeException e) {
            resp.setStatusCode(400); // Bad Request
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500); // Internal Server Error
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
}


    @DeleteMapping("/deleteFixedAsset/{id}")
    public ResponseEntity<ReqRes> deleteFixedAsset(@PathVariable Long id){
        ReqRes resp = new ReqRes();
        try {
            fixedAssetService.deleteFixedAsset(id);
            resp.setMessage("Fixed Asset Deleted Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getAllFixedAssets")
    public ResponseEntity<ReqRes> getAllFixedAssets() {
    ReqRes resp = new ReqRes();
    try {
        List<FixedAsset> fixedAssets = fixedAssetService.getAllFixedAssets();
        resp.setFixedAssets(fixedAssets); 
        resp.setMessage("Fixed Assets Retrieved Successfully");
        resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getFixedAssetById/{id}")
    public ResponseEntity<ReqRes> getFixedAssetById(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            Optional<FixedAsset> fixedAsset = fixedAssetService.getFixedAssetById(id);
            if (fixedAsset.isPresent()) {
                resp.setFixedAsset(fixedAsset.get());
                resp.setMessage("Fixed Asset Retrieved Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Fixed Asset Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);

    }

    @PutMapping("/updateFixedAsset/{id}")
    public ResponseEntity<ReqRes> updateFixedAsset(@PathVariable Long id, @RequestBody FixedAssetRequest fixedAssetRequest) {
        ReqRes resp = new ReqRes();
        try {
            // Fetch the existing FixedAsset entity from the database
            Optional<FixedAsset> existingFixedAssetOpt = fixedAssetRepository.findById(id);
            if (!existingFixedAssetOpt.isPresent()) {
                resp.setStatusCode(404);
                resp.setMessage("Fixed Asset not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
            }
            FixedAsset existingFixedAsset = existingFixedAssetOpt.get();

            // Update the fields that are present in the request
            if (fixedAssetRequest.getName() != null) {
                existingFixedAsset.setName(fixedAssetRequest.getName());
            }
            if (fixedAssetRequest.getModel() != null) {
                existingFixedAsset.setModel(fixedAssetRequest.getModel());
            }
            if (fixedAssetRequest.getYear() != null) {
                existingFixedAsset.setYear(fixedAssetRequest.getYear());
            }
            if (fixedAssetRequest.getPrice() != null) {
                existingFixedAsset.setPrice(fixedAssetRequest.getPrice());
            }
            if (fixedAssetRequest.getSerialNumber() != null) {
                existingFixedAsset.setSerialNumber(fixedAssetRequest.getSerialNumber());
            }
            if (fixedAssetRequest.getPurchaseDate() != null) {
                existingFixedAsset.setPurchaseDate(fixedAssetRequest.getPurchaseDate());
            }
            if (fixedAssetRequest.getUnit() != null) {
                existingFixedAsset.setUnit(fixedAssetRequest.getUnit());
            }
            if (fixedAssetRequest.getQuantity() != null) {
                existingFixedAsset.setQuantity(fixedAssetRequest.getQuantity());
            }
            if (fixedAssetRequest.getImage() != null) {
                existingFixedAsset.setImage(fixedAssetRequest.getImage());
            }
            if (fixedAssetRequest.getStatus() != null) {
                existingFixedAsset.setStatus(fixedAssetRequest.getStatus());
            }
            if (fixedAssetRequest.getStatustext() != null) {
                existingFixedAsset.setStatustext(fixedAssetRequest.getStatustext());
            }

            // Update the Category if present
            Integer categoryId = fixedAssetRequest.getCategoryId();
            if (categoryId != null) {
                Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
                if (categoryOpt.isPresent()) {
                    existingFixedAsset.setCategory(categoryOpt.get());
                } else {
                    throw new RuntimeException("Category not found");
                }
            }

            // Update the AssetHolder if present
            Long assetHolderId = fixedAssetRequest.getAssetHolder();
            if (assetHolderId != null) {
                Optional<AssetHolder> assetHolderOpt = assetHolderRepository.findById(assetHolderId);
                if (assetHolderOpt.isPresent()) {
                    existingFixedAsset.setAssetHolder(assetHolderOpt.get());
                } else {
                    throw new RuntimeException("AssetHolder not found");
                }
            } else {
                existingFixedAsset.setAssetHolder(null); // or handle as needed
            }

            // Save the updated FixedAsset
            FixedAsset updatedFixedAsset = fixedAssetRepository.save(existingFixedAsset);

            // Prepare response
            resp.setFixedAsset(updatedFixedAsset);
            resp.setMessage("Fixed Asset Updated Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getFixedAssetsByDepartment/{departmentId}")
    public ResponseEntity<ReqRes> getFixedAssetsByDepartment(@PathVariable Long departmentId) {
        ReqRes resp = new ReqRes();
        try {
            List<FixedAsset> fixedAssets = fixedAssetService.getAllFixedAssetsWithDepartment(departmentId);
            resp.setFixedAssets(fixedAssets);
            resp.setMessage("Fixed Assets Retrieved Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

}