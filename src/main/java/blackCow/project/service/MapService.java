package blackCow.project.service;

import blackCow.project.domain.Building;
import blackCow.project.domain.FloorInfo;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MapService {
    public ResponseEntity<Resource> getFloorImage(int buildingId, String floor);

    public ResponseEntity<FloorInfo> getNumOfFloors(int buildingId);
//    public List<Building> getAllBuildings();
}
