package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;

public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String contentType;
    private Long size;
    private String url;

    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = "attachment_" + attachment.getId(); // Placeholder, as filename is not stored
        this.contentType = attachment.getContentType();
        this.size = (long) attachment.getData().length;
        this.url = "/attachments/" + attachment.getId(); // URL to access the attachment
    }
}
