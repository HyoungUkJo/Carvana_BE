package com.carvana.global.storage.service;

import okio.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

// TODO : 오류를 커스텀 exception으로 변경
@Service
public class StorageService {
    private final S3Client s3Client;

    private final String bucketName;

    public StorageService(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    // 파일 업로드
    public String uploadFile(MultipartFile file, String category, String entityUUID) {
        try {
            // 추후 originFilename이 아닌 이름을 인자로 받아서 사용할 수도 있음
            String objectKey = category + "/" + entityUUID + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // fileUpload
            s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(file.getContentType()) // 파일 타입 지정
                .build(), RequestBody.fromBytes(file.getBytes()));

            return objectKey;
        }  catch (Exception e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }

    }

    // 파일 삭제
    /*
    * 삭제를 Soft Delete를 하고 추후에 이벤트로 등록시킬지 확인
    * */
    public void deleteFile(String objectKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 오류 발생", e);
        }
    }

}
