package blackCow.project;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//aws
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Configuration
@ComponentScan
public class AppConfig {
//    @Value("${cloud.aws.s3.endpoint}")
//    private String s3Endpoint;
//
//    @Value("${cloud.aws.region}")
//    private String region;

    @Bean
    public AmazonS3 amazonS3(){
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.AP_SOUTH_1) // 필요한 AWS 리전으로 설정
//                .withCredentials(new AWSStaticCredentialsProvider(
//                        new BasicAWSCredentials("your-access-key", "your-secret-key")))
//                .build();

        //cloud9에서
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1) // 본인의 리전으로 설정
                .build();
    }


}

