package com.file.FileDemoApplication.controller;


import com.file.FileDemoApplication.payload.FileResponse;
import com.file.FileDemoApplication.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("image") MultipartFile image) throws IOException {
        String filename = null;
        try{
            filename = this.fileService.uploadImage(path,image);
        } catch (Exception e){
            return new ResponseEntity<>(new FileResponse(null,"Image is not uploaded due to some error on server !!"),
                                                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileResponse(filename,"Image is successfully uploaded !!"),
                                                        HttpStatus.OK);
    }
}
