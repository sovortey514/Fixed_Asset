package com.twd.SpringSecurityJWT.controller;

import com.twd.SpringSecurityJWT.entity.Building;

import com.twd.SpringSecurityJWT.service.BuildingService;

import com.twd.SpringSecurityJWT.dto.ReqRes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @PostMapping("/createBuilding")
    public ResponseEntity<ReqRes> createBuilding(@RequestBody Building building) {
        ReqRes resp = new ReqRes();
        try {
            Building createdBuilding = buildingService.creatBuilding(building);
            resp.setBuilding(createdBuilding); // Ensure this method exists
            resp.setMessage("Building Created Successfully");
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

    @DeleteMapping("/deleteBuilding/{id}")
    public ResponseEntity<ReqRes> deleteBuilding(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        try {
            buildingService.deleteBuilding(id);
            resp.setMessage("Building Deleted Successfully");
            resp.setStatusCode(200);
        } catch (IllegalArgumentException e) {
            resp.setStatusCode(404);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getAllBuildings")
    public ResponseEntity<ReqRes> getAllBuildings() {
        ReqRes resp = new ReqRes();
        try {
            List<Building> buildings = buildingService.getAllBuildings();
            resp.setBuildings(buildings); // Ensure this method exists
            resp.setMessage("Buildings Retrieved Successfully");
            resp.setStatusCode(200);
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/getBuildingById/{id}")
    public ResponseEntity<ReqRes> getBuildingById(@PathVariable Long id) {
        ReqRes resp = new ReqRes();
        if (id <= 0) {
            resp.setMessage("Invalid ID");
            resp.setStatusCode(400);
            return ResponseEntity.badRequest().body(resp);
        }
        try {
            Optional<Building> building = buildingService.getBuildingById(id);
            if (building.isPresent()) {
                resp.setBuilding(building.get()); // Ensure this method exists
                resp.setMessage("Building Retrieved Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Building Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/updateBuilding/{id}")
    public ResponseEntity<ReqRes> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        ReqRes resp = new ReqRes();
        try {
            Building updatedBuilding = buildingService.updateBuilding(id, building);
            if (updatedBuilding != null) {
                resp.setBuilding(updatedBuilding); // Ensure this method exists
                resp.setMessage("Building Updated Successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("Building Not Found");
                resp.setStatusCode(404);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}