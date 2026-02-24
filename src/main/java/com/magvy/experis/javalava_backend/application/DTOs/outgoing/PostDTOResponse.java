package com.magvy.experis.javalava_backend.application.DTOs.outgoing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.enums.MediaType;
import com.magvy.experis.javalava_backend.domain.util.AttachmentUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDTOResponse {
    private Long id;

    private UserDTOResponse user;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime published;

    private boolean visible;

    private AttachmentDTO attachment;

    private boolean liked;

    private int likeCount;

    private int commentCount;

    public PostDTOResponse(Post post, int likeCount, int commentCount) {
        this.id = post.getId();
        this.user = new UserDTOResponse(post.getUser());
        this.content = post.getContent();
        this.published = post.getPublished();
        this.visible = post.isVisible();
        this.attachment = (post.getAttachment() != null) ? new AttachmentDTO(post.getAttachment()) : null;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostDTOResponse(Post post, boolean liked, int likeCount, int commentCount) {
        this.id = post.getId();
        this.user = new UserDTOResponse(post.getUser());
        this.content = post.getContent();
        this.published = post.getPublished();
        this.visible = post.isVisible();
        this.liked = liked;
        this.attachment = (post.getAttachment() != null) ? new AttachmentDTO(post.getAttachment()) : null;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostDTOResponse(Post post) {
        this.id = post.getId();
        this.user = new UserDTOResponse(post.getUser());
        this.content = post.getContent();
        this.published = post.getPublished();
        this.attachment = (post.getAttachment() != null) ? new AttachmentDTO(post.getAttachment()) : null;
        this.visible = post.isVisible();
    }
}
