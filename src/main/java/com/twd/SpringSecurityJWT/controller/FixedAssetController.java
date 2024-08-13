package com.twd.SpringSecurityJWT.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.repository.CategoryRepository;
import com.twd.SpringSecurityJWT.service.FixedAssetService;

@RestController
@RequestMapping("/admin")
public class FixedAssetController {
    
    @Autowired
    private FixedAssetService fixedAssetService;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @PostMapping("/createFixedAsset")
    public ResponseEntity<ReqRes> createFixedAsset(@RequestBody FixedAssetRequest fixedAssetRequest) {
        ReqRes resp = new ReqRes();
        try {
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

            // fixedAssetToSave.setStatustext(fixedAssetRequest.getStatustext());
        
            Integer categoryId = fixedAssetRequest.getCategoryId();
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                fixedAssetToSave.setCategory(categoryOpt.get());
            } else {
                throw new RuntimeException("Category not found");
            }

            FixedAsset savedFixedAsset = fixedAssetService.createFixedAsset(fixedAssetToSave, categoryId);
            if (savedFixedAsset != null && savedFixedAsset.getId() != null) {
                resp.setFixedAsset(savedFixedAsset);  // Ensure this method exists
                resp.setMessage("Fixed Asset Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
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
            System.err.println(fixedAssetRequest);
            System.err.println(fixedAssetRequest.getName());

            FixedAsset fixedAssetToUpdate = new FixedAsset();
            fixedAssetToUpdate.setName(fixedAssetRequest.getName());
            fixedAssetToUpdate.setModel(fixedAssetRequest.getModel());
            fixedAssetToUpdate.setYear(fixedAssetRequest.getYear());
            fixedAssetToUpdate.setPrice(fixedAssetRequest.getPrice());
            fixedAssetToUpdate.setSerialNumber(fixedAssetRequest.getSerialNumber());
            fixedAssetToUpdate.setPurchaseDate(fixedAssetRequest.getPurchaseDate());
            fixedAssetToUpdate.setUnit(fixedAssetRequest.getUnit());
            fixedAssetToUpdate.setQuantity(fixedAssetRequest.getQuantity());
            fixedAssetToUpdate.setImage(fixedAssetRequest.getImage());
            fixedAssetToUpdate.setStatus(fixedAssetRequest.getStatus());
            
            Integer categoryId = fixedAssetRequest.getCategoryId();
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                fixedAssetToUpdate.setCategory(categoryOpt.get());
            } else {
                throw new RuntimeException("Category not found");
            }

            FixedAsset updatedFixedAsset = fixedAssetService.updateFixedAsset(id, fixedAssetToUpdate);
            resp.setFixedAsset(updatedFixedAsset);
            resp.setMessage("Fixed Asset Updated Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }


}



