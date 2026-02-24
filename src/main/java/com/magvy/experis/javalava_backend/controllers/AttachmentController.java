package com.magvy.experis.javalava_backend.controllers;

import com.magvy.experis.javalava_backend.application.DTOs.outgoing.AttachmentDTO;
import com.magvy.experis.javalava_backend.controllers.util.ResponseUtil;
import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.domain.util.AttachmentUtil;
import com.magvy.experis.javalava_backend.infrastructure.repositories.AttachmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable("id") Long id){
        Attachment attachment = attachmentUtil.findByIdOrThrow(id);
        AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
        return ResponseUtil.wrapEntity(attachmentDTO); // Comment
    }

}
