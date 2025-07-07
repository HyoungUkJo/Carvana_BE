package com.carvana.global.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@PropertySource("classpath:config/secrets.properties")
@Configuration
public class StorageConfig {
    @Value("${MINIO_URL}")
    private String storageUrl;        // 연결할 minio db url

    @Value("${MINIO_ACCESS_KEY}")
    private String accessKey;       //  accesskey(Id)

    @Value("${MINIO_SECRET_KEY}")
    private String secretKey;       // secretKey(Password)

    @Value("${STORAGE_BUKET}")
    private String bucketName;     // 연결할 버켓 이

    // 업로드 및 삭제를 위한 s3Client Bean 등록
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2)
            .endpointOverride(URI.create(storageUrl))   // s3로 전환시 제연
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
    }

    // 버켓 이름을 가지고 올 bean 등록
    @Bean
    public String bucketName() {
        return bucketName;
    }

    // PresginedUrl 생성을 위한 Bean 등록
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            .endpointOverride(URI.create(storageUrl))   // s3로 전환시 제연
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();
    }
}
