package com.twd.SpringSecurityJWT.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twd.SpringSecurityJWT.dto.ReqRes;
import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import com.twd.SpringSecurityJWT.entity.FixedAssetDetail;
import com.twd.SpringSecurityJWT.service.FixedAssetDetailService;

@RestController
@RequestMapping("/admin")
public class FixedAssetDetailController {
    
    @Autowired
    private FixedAssetDetailService fixedAssetDetailService;

     @PostMapping("/createdetail")
    public ResponseEntity<ReqRes> createfixedAssetdeatil(@RequestBody FixedAssetDetail fixedAssetDetail) {
        ReqRes resp = new ReqRes();
        try {
            FixedAssetDetail createfixedAssetdeatil = fixedAssetDetailService.createFixedAssetDetail(fixedAssetDetail);
            resp.setFixedAssetDetail(createfixedAssetdeatil);
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

    @GetMapping("/getcreatedetail")
    public ResponseEntity<ReqRes> getAllFixedAssetDetail() {
        ReqRes resp = new ReqRes();
        try {
            List<FixedAssetDetail> fixedAssetDetails = fixedAssetDetailService.getAllFixedAssetDetails();
            resp.setFixedAssetDetails(fixedAssetDetails);
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

    
}
