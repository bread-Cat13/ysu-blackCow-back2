package blackCow.project.controller;


import blackCow.project.domain.FloorInfo;
import blackCow.project.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/map/buildings")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/floors")
    public ResponseEntity<org.springframework.core.io.Resource> getFloorInfo2(
            @RequestParam int bId,
            @RequestParam String f) {
        log.info("buildingId = {} / floor = {}", bId, f);
        return mapService.getFloorImage(bId, f);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{buildingId}/floor-info")
    public ResponseEntity<FloorInfo> numOfFloors(@PathVariable int buildingId){
        log.info("buildingId = {}", buildingId);
        return mapService.getNumOfFloors(buildingId);
    }
}



