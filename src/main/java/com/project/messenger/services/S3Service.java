package com.project.messenger.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.messenger.models.enums.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.annotation.MergedAnnotation.Adapt.values;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String directory) {
        try {
            String fileName = generateFileName(file, directory);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

//            PutObjectRequest request = new PutObjectRequest(bucketName,
//                    fileName,
//                    file.getInputStream(),
//                    metadata
//            ).withCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectRequest request = new PutObjectRequest(bucketName,
                    fileName,
                    file.getInputStream(),
                    metadata);

            s3Client.putObject(request);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String generateFileName(MultipartFile file, String directory) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && !originalFileName.isEmpty()) {
            int lastDotIndex = originalFileName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFileName.substring(lastDotIndex);
            }
        }
        // Если расширение не найдено, пытаемся определить по contentType
        if (extension.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType != null) {
                extension = FileType.getByContentType(contentType)
                        .map(FileType::getExtension)
                        .orElse("");
            }
        }
        if (extension.isEmpty()) {
            throw new IllegalArgumentException("Could not determine file extension");
        }
        return directory + "/" + UUID.randomUUID() + extension;
    }

    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    public String getFileUrl(String fileName) {
        return s3Client.getUrl(bucketName, fileName).toString();
    }

}
