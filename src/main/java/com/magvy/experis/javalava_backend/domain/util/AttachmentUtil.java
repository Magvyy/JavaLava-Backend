package com.magvy.experis.javalava_backend.domain.util;

import com.magvy.experis.javalava_backend.domain.entitites.Attachment;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.enums.MediaType;
import com.magvy.experis.javalava_backend.domain.exceptions.AttachmentException;
import com.magvy.experis.javalava_backend.domain.exceptions.InvalidMediaTypeException;
import com.magvy.experis.javalava_backend.domain.exceptions.PostException;
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
    public Attachment createAttachment(MultipartFile file) throws InvalidMediaTypeException {
        if (file.isEmpty()) {
            return null;
        }
        Attachment attachment = convertToEntity(file);

        // Add the media type to the attachment entity
        MediaType mediaType = determineMediaType(attachment);
        System.out.println("Media type: " + mediaType);
        if (mediaType == MediaType.OTHER || mediaType == null) {
            throw new InvalidMediaTypeException(mediaType == null ? "null" : mediaType.name(), HttpStatus.BAD_REQUEST);
        }
        attachment.setMediaType(mediaType);
        return attachmentRepository.save(attachment);
    }

    public static MediaType determineMediaType(Attachment attachment) {
        if (attachment == null) return null;
        String contentType = attachment.getContentType();
        if (contentType.startsWith("image/")) {
            return MediaType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return MediaType.VIDEO;
        } else if (contentType.startsWith("audio/")) {
            return MediaType.AUDIO;
        } else if (contentType.equals("application/pdf") || contentType.equals("application/msword") || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            return MediaType.DOCUMENT;
        } else {
            return MediaType.OTHER;
        }
    }

    public ResponseEntity<Attachment> getAttachmentById(Long id){
        return new ResponseEntity<>(attachmentRepository.getReferenceById(id), HttpStatus.OK);
    }

    public Attachment findByIdOrThrow(Long id) {
        return attachmentRepository.findById(id).orElseThrow(() -> new AttachmentException("Attachment not found", HttpStatus.NOT_FOUND));
    }
}
