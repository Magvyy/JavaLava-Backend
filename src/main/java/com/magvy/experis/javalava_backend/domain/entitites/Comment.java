package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Timestamp published;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    public Comment(String content, LocalDateTime published, Post post, User user) {
        this.content = content;
        this.published = Timestamp.valueOf(published);
        this.post = post;
        this.user = user;
    }

    public Comment() {}
}
