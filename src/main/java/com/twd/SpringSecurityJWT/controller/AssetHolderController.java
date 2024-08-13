package com.twd.SpringSecurityJWT.controller;

import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.AssetHolder;
import com.twd.SpringSecurityJWT.service.AssetHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AssetHolderController {

    @Autowired
    private AssetHolderService assetHolderService;

    @PostMapping("/createassetholder")
    public ResponseEntity<ReqRes> createAssetHolder(@RequestBody AssetHolder assetHolder) {
        ReqRes resp = new ReqRes();
        try {
            AssetHolder createdAssetHolder = assetHolderService.createAssetHolder(assetHolder);
            resp.setAssetHolder(createdAssetHolder);
            resp.setMessage("AssetHolder Created Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(400);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getallassetholders")
    public ResponseEntity<ReqRes> getAllAssetHolders() {
        ReqRes resp = new ReqRes();
        try {
            List<AssetHolder> assetHolders = assetHolderService.getAllAssetHolders();
            resp.setAssetHolders(assetHolders);
            resp.setMessage("AssetHolders retrieved successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getassetholderbyId/{id}")
    public ResponseEntity<AssetHolder> getAssetHolderById(@PathVariable Long id) {
        Optional<AssetHolder> assetHolder = assetHolderService.getAssetHolderById(id);
        return assetHolder.map(holder -> new ResponseEntity<>(holder, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/updateassetholderbyId/{id}")
    public ResponseEntity<ReqRes> updateAssetHolder(@PathVariable Long id, @RequestBody AssetHolder assetHolder) {
        ReqRes resp = new ReqRes();
        try {
            AssetHolder updatedAssetHolder = assetHolderService.updateAssetHolder(id, assetHolder);
            resp.setAssetHolder(updatedAssetHolder);
            resp.setMessage("AssetHolder Updated Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setMessage("AssetHolder Not Found");
            resp.setStatusCode(404);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }


    @DeleteMapping("/deleteassetbyId/{id}")
    public ResponseEntity<ReqRes> deleteAssetHolder(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            assetHolderService.deleteAssetHolder(id);
            resp.setMessage("Assetholder Deleted Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(404);
            resp.setError(e.getMessage());
        }catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
