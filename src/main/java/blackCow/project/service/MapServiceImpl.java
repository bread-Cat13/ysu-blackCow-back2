package blackCow.project.service;

import blackCow.project.domain.FloorInfo;
import blackCow.project.exception.BuildingNotFoundException;
import blackCow.project.exception.FloorNotFoundException;
import blackCow.project.repository.BuildingRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-env.properties")
public class MapServiceImpl implements MapService{

    private final AmazonS3 s3Client;
    private final BuildingRepository buildingRepository;


    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    public ResponseEntity<Resource> getFloorImage(int buildingId, String floor) {
        //유효성 검사
        //건물번호 + 층수 검사
        if(!buildingRepository.isValidBuilding(buildingId))
            throw new BuildingNotFoundException("NOT VALID BuildingId");
        else if(!buildingRepository.isValidFloor(buildingId, floor)){
            String msg;
            FloorInfo floorInfo = buildingRepository.getFloorInfo(buildingId);

            if(floorInfo.getMinFloor()==0)
                msg = "F1~F"+floorInfo.getMaxFloor();
            else
                msg = "B"+(-1*floorInfo.getMinFloor())+"~F"+floorInfo.getMaxFloor();

            throw new FloorNotFoundException("NOT VALID FLOOR[building = "+buildingRepository.getBuildingName(buildingId)
                    +"/floor = "+msg+"]");
        }


        //제4공학관을 제1공학관으로 매핑
        if(buildingId == 124) {
           buildingId = 121;
        }
        // S3에서 객체 가져오기
        //지하 검색 시 b,B상관없게 처리해주기
        if(floor.startsWith("B"))
            floor = "b" + floor.substring(1);
        String key = buildingId + "/" + floor + ".png";
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        // Resource 객체 생성
        Resource resource = new InputStreamResource(objectInputStream);

        // HTTP 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        httpHeaders.setContentLength(s3Object.getObjectMetadata().getContentLength());

        //Cache-content
        httpHeaders.setCacheControl("max-age=120"); //120초 유지

        //Last-modified
        Date lastModified = s3Object.getObjectMetadata().getLastModified(); //s3 객체 마지막 수정 시간 적용하기
        httpHeaders.setLastModified(lastModified.getTime());


        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);
    }

    @Override
    public ResponseEntity<FloorInfo> getNumOfFloors(int buildingId) {
        FloorInfo floorInfo = buildingRepository.getFloorInfo(buildingId);
        log.info("min floor = {} / max floor = {}", floorInfo.getMinFloor(), floorInfo.getMaxFloor());
        return new ResponseEntity<>(floorInfo, HttpStatus.OK);
    }

}
