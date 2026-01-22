    package com.magvy.experis.javalava_backend.domain.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private Date published;
    private boolean visible;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(String content, Date published, boolean visible, User user) {
        this.content = content;
        this.published = published;
        this.visible = visible;
        this.user = user;
    }

    public Post() {}
}
