package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

    @Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private Timestamp published;
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(String content, LocalDateTime published, boolean visible, User user) {
        this.content = content;
        this.published = Timestamp.valueOf(published);
        this.visible = visible;
        this.user = user;
    }

    public Post() {}
}
