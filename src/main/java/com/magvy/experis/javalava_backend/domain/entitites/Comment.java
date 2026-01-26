    package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

    @Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private Timestamp published;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String content, LocalDateTime published, Post post, User user) {
        this.content = content;
        this.published = Timestamp.valueOf(published);
        this.post = post;
        this.user = user;
    }
}
