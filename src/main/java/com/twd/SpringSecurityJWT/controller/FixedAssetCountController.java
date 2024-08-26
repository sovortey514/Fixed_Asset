package com.twd.SpringSecurityJWT.controller;


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
import com.twd.SpringSecurityJWT.service.FixedAssetCountService;

import java.util.List;

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
            resp.setStatusCode(HttpStatus.CREATED.value());
        } catch (Exception e) {
            resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setError(e.getMessage());
        }
        return ResponseEntity.status(resp.getStatusCode()).body(resp);
    }

    @GetMapping("/getAllfixedassetcount")
    public ResponseEntity<List<FixedAssetCounts>> getAllFixedAssetCounts() {
        List<FixedAssetCounts> fixedAssetCountsList = fixedAssetCountService.getAllFixedAssetCounts();
        return ResponseEntity.ok(fixedAssetCountsList);
    }
}
