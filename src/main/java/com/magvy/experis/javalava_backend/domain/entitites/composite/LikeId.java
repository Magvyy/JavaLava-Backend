package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class LikeId implements Serializable {
    private Long userId;
    private Long postId;

    LikeId(){};
    public LikeId(User user, Post post) {
        this.userId = user.getId();
        this.postId = post.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        LikeId that = (LikeId) o;
        return this.userId.equals(that.userId) && this.postId.equals(that.postId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode() + postId.hashCode();
    }
}
