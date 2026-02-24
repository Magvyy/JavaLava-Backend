package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.infrastructure.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private static final String UPLOAD_DIR = "C:/uploads/";

    public AttachmentService(AttachmentRepository attachmentRepository){
        this.attachmentRepository = attachmentRepository;
    }

    private Attachment convertToEntity(MultipartFile attachment){
        return new Attachment(
                null,
                attachment
        );
    }
    public Attachment createAttachment(MultipartFile file){
        if (file.isEmpty()) {
            return null;
        }
        System.out.println("3");
        Attachment attachment = convertToEntity(file);
        System.out.println("4");
        return attachmentRepository.save(attachment);
    }

    public ResponseEntity<Attachment> getAttachmentById(Long id){
        return new ResponseEntity<>(attachmentRepository.getReferenceById(id), HttpStatus.OK);
    }
}
