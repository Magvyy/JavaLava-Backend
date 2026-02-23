package com.magvy.experis.javalava_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class AttatchmentService {

    private static final String UPLOAD_DIR = "C:/uploads/";
    public ResponseEntity<String> uploadFile(MultipartFile file){
        try{
            if (file.isEmpty()){
                return ResponseEntity.badRequest().body("File is empty");
            }
            File dir = new File(UPLOAD_DIR);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = UPLOAD_DIR + file.getOriginalFilename();

            file.transferTo(new File(filePath));

            return ResponseEntity.status(HttpStatus.OK).body("file uploaded successfuly");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file" + e.getMessage());
            }
    }
}
