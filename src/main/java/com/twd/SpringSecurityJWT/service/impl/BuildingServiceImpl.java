package com.twd.SpringSecurityJWT.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.twd.SpringSecurityJWT.entity.Building;
import com.twd.SpringSecurityJWT.repository.BuildingRepository;
import com.twd.SpringSecurityJWT.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public Building creatBuilding(Building building) {
        validateBuilding(building);
        building.setName(building.getName());
        building.setCreatedAt(LocalDateTime.now());
        building.setUpdatedAt(LocalDateTime.now());
        return buildingRepository.save(building);
    }

    private void validateBuilding(Building building) {
        if (building.getName() == null || building.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Building name must not be empty");
        }
    }

    @Override
    public void deleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new IllegalArgumentException("Building not found with ID: " + id);
        }
        buildingRepository.deleteById(id);
    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    public Optional<Building> getBuildingById(Long id) {
        return buildingRepository.findById(id);
    }

    @Override
    public Building updateBuilding(Long id, Building building) {
        if (!buildingRepository.existsById(id)) {
            throw new ResourceAccessException("Building not found with ID: " + id);
        }
        validateBuilding(building);
        building.setId(id);
        building.setUpdatedAt(LocalDateTime.now());
        return buildingRepository.save(building);
    }
}
