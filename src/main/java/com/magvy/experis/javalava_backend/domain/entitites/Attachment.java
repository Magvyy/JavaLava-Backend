package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.enums.MediaType;
import com.magvy.experis.javalava_backend.domain.util.AttachmentUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Getter
@Setter
@Table(name = "attachments")
public class Attachment     {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "media_type", nullable = true)
    private MediaType mediaType;

    @Column(name = "url", nullable = true)
    private String url;

    //@Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    public Attachment(){}
    public Attachment(Long id, MultipartFile attachment) {
        this.id = id;
        this.contentType = attachment.getContentType();
        try {
            this.data = attachment.getBytes();
        } catch (IOException e) {
            this.data = null;
        }
    }
}
