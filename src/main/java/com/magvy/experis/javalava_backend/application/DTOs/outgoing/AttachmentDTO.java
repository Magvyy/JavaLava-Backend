package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import lombok.Getter;


@Getter
public class AttachmentDTO {
    private final Long id;
    private final String fileName;
    private final String contentType;
    private final Long size;
    private final String url;

    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = "attachment_" + attachment.getId(); // Placeholder, as filename is not stored
        this.contentType = attachment.getContentType();
        this.size = (long) attachment.getData().length;
        this.url = "/attachments/" + attachment.getId(); // URL to access the attachment
    }
}
