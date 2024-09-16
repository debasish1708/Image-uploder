package com.file.FileDemoApplication.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FunctionalInterface
public interface FileService {
    String uploadImage(String path,MultipartFile file) throws IOException;
}
