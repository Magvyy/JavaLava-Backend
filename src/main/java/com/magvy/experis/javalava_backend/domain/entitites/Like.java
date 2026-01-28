    package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@IdClass(LikeId.class)
@Table(name = "likes")
public class Like {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Like() {};
    public Like(User user, Post post){
        this.post = post;
        this.user = user;
    }

}
