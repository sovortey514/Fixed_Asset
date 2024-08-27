package com.twd.SpringSecurityJWT.controller;


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

import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.AssetHolder;
import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import com.twd.SpringSecurityJWT.service.FixedAssetCountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class FixedAssetCountController {

    @Autowired
    private FixedAssetCountService fixedAssetCountService;
    
    @PostMapping("/createcount")
    public ResponseEntity<ReqRes> createFixedAssetCount(@RequestBody FixedAssetCounts fixedAssetCounts) {
        ReqRes resp = new ReqRes();
        try {
            FixedAssetCounts createdFixedAssetCounts = fixedAssetCountService.createFixedAssetCounts(fixedAssetCounts);
            resp.setFixedAssetCounts(createdFixedAssetCounts);
            resp.setMessage("FixedAssetCount Created Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(400);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/getcreatecount")
    public ResponseEntity<ReqRes> getAllFixedAssetCounts() {
        ReqRes resp = new ReqRes();
        try {
            List<FixedAssetCounts> fixedAssetCountsList = fixedAssetCountService.getAllFixedAssetCounts();
            resp.setFixedAssetCounts2(fixedAssetCountsList);
            resp.setMessage("Fixed Asset Counts retrieved successfully");
            resp.setStatusCode(200);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
            // Log the exception (consider using a logging framework)
            System.err.println("Error retrieving fixed asset counts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
    @GetMapping("/getfixedassetcountbyId/{id}")
    public ResponseEntity<FixedAssetCounts> getAllFixedAssetCountsById(@PathVariable Long id) {
        Optional<FixedAssetCounts> optionalFixedAssetCounts = fixedAssetCountService.getAllFixedAssetCountsById(id);
        return optionalFixedAssetCounts
                .map(holder -> new ResponseEntity<>(holder, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // @DeleteMapping("/deleteassetbyId/{id}")
    // public ResponseEntity<ReqRes> deleteFixedAssetCount(@PathVariable Long id) {
    //     ReqRes resp = new ReqRes();
    //     try {
    //         fixedAssetCountService.deleteFixedAssetCount(id);
    //         resp.setMessage("Assetholder Deleted Successfully");
    //         resp.setStatusCode(200);
    //     } catch (IllegalArgumentException e) {
    //         resp.setStatusCode(404);
    //         resp.setError(e.getMessage());
    //     }catch (Exception e) {
    //         resp.setStatusCode(500);
    //         resp.setError(e.getMessage());
    //     }
    //     return ResponseEntity.ok(resp);
    // }
    

}
