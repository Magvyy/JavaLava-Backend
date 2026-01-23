    package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(String content, Date date, Post post, User user) {
        this.content = content;
        this.date = date;
        this.post = post;
        this.user = user;
    }
}
