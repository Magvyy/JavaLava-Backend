package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.domain.util.AttachmentUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentUtil attachmentUtil;

    public AttachmentController(AttachmentUtil attachmentUtil){
        this.attachmentUtil = attachmentUtil;
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable("id") Long id){
        Attachment attachment = attachmentUtil.findByIdOrThrow(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(attachment.getContentType()));
        return new ResponseEntity<>(attachment.getData(), headers, HttpStatus.OK); // Comment
    }
}
