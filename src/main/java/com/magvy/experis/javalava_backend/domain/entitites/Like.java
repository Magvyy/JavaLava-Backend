package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.domain.entitites.composite.LikeId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {
    @EmbeddedId
    private LikeId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Like() {};
    public Like(User user, Post post){
        this.post = post;
        this.user = user;
    }

}
