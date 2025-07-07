package com.carvana.global.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
public class PresignedUrlService {
    private final S3Presigner presigner;
    private final String bucketName;

    public PresignedUrlService(S3Presigner presigner, String bucketName) {
        this.presigner = presigner;
        this.bucketName = bucketName;
    }

    // presignedUrl 생성
    public String generatePresignedUrl(String objectKey, int expireMinutes) {
        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(expireMinutes))
            .getObjectRequest(b -> b.bucket(bucketName).key(objectKey))
            .build());

        return presignedGetObjectRequest.url().toString();
    }
}
