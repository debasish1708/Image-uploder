package com.file.FileDemoApplication.controller;

import com.file.FileDemoApplication.services.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final ImageUploader imageUploader;

    //upload image
    @PostMapping
    public List<String> uploadImage(@RequestParam MultipartFile file) {
          imageUploader.uploadImage(file);
          return imageUploader.allFiles();
    }

    // get all files
    @GetMapping
    public List<String> getAllFiles(){
        return imageUploader.allFiles();
    }

    @GetMapping("/{fileName}")
    public String urlByName(@PathVariable("fileName") String fileName) {
        return imageUploader.getImageUrlByName(fileName);
    }
}
