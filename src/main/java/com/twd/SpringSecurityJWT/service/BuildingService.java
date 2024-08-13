
package com.twd.SpringSecurityJWT.service;

import java.util.List;
import java.util.Optional;
import com.twd.SpringSecurityJWT.entity.Building;

public interface BuildingService {
    Building creatBuilding(Building building);
    void deleteBuilding(Long id);
    List<Building> getAllBuildings();
    Optional<Building> getBuildingById(Long id);
    Building updateBuilding(Long id, Building building);
}
