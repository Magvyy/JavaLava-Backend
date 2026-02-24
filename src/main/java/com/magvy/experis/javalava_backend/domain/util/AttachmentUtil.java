package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.infrastructure.repositories.AttachmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AttachmentUtil {

    private final AttachmentRepository attachmentRepository;

    public AttachmentUtil(AttachmentRepository attachmentRepository){
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
