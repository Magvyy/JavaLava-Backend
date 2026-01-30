package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;

import java.io.Serializable;

public class LikeId implements Serializable {
    private User user;
    private Post post;

    LikeId(){};
    public LikeId(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        LikeId that = (LikeId) o;
        return this.user.getId() == that.user.getId() && this.post.getId() == that.post.getId();
    }

    @Override
    public int hashCode() {
        return user.hashCode() + post.hashCode();
    }
}
