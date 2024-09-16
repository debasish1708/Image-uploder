package com.file.FileDemoApplication.services;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface ImageUploader {
    String uploadImage(MultipartFile file);
    List<String> allFiles();
    String preSignedUrl(String fileName);
    String getImageUrlByName(String fileName);
}
