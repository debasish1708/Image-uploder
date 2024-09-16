package com.file.FileDemoApplication.services.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.file.FileDemoApplication.exceptions.ImageUploadException;
import com.file.FileDemoApplication.services.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3ImageUploader implements ImageUploader {

    private final AmazonS3 client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadImage(MultipartFile image) {
        if(image==null)throw new ImageUploadException("Image cannot be null");
        String actualFileName = image.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + actualFileName.
                                                            substring(actualFileName.lastIndexOf('.'));
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(image.getSize());
        try {
            @SuppressWarnings("unused")
            PutObjectResult putObjectResult = client.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metaData));
            return this.preSignedUrl(fileName);
        } catch (IOException e) {
            throw new ImageUploadException("error in uploading image"+e.getMessage());
        }
    }

    @Override
    public List<String> allFiles() {
        ListObjectsV2Request listObjectRequest = new ListObjectsV2Request()
                .withBucketName(bucketName);
        ListObjectsV2Result listObjectsV2Result = client.listObjectsV2(listObjectRequest);
        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
        List<String> listOfFilesUrls = objectSummaries.stream().map(item->this.preSignedUrl(item.getKey())).toList();
        return listOfFilesUrls;
    }

    @Override
    public String preSignedUrl(String fileName) {
        Date expirationDate = new Date();
        long time=expirationDate.getTime();
        int hour=2;
        time=time+hour*60*60*1000;
        expirationDate.setTime(time);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expirationDate);
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Override
    public String getImageUrlByName(String fileName) {
        S3Object object = client.getObject(bucketName, fileName);
        String key = object.getKey();
        String url = preSignedUrl(key);
        return url;
    }
}
