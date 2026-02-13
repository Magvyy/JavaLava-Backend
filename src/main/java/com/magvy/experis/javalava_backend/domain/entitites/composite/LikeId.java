package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;

import java.io.Serializable;

public class LikeId implements Serializable {
    private Long user;
    private Long post;

    LikeId(){};
    public LikeId(User user, Post post) {
        this.user = user.getId();
        this.post = post.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        LikeId that = (LikeId) o;
        return this.user.equals(that.user) && this.post.equals(that.post);
    }

    @Override
    public int hashCode() {
        return user.hashCode() + post.hashCode();
    }
}
