package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.enums.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Getter
@Setter
@Table(name = "attatchments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "url", nullable = true)
    private String url;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

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
