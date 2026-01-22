package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.domain.entitites.Post;
import com.magvy.experis.javalava_backend.domain.entitites.User;

import java.io.Serializable;

public class FriendId implements Serializable {
    private User user1;
    private User user2;

    public FriendId(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        FriendId that = (FriendId) o;
        return
                   this.user1.getId() == that.user1.getId() && this.user2.getId() == that.user2.getId()
                || this.user1.getId() == that.user2.getId() && this.user2.getId() == that.user1.getId();
    }

    @Override
    public int hashCode() {
        return user1.hashCode() + user2.hashCode();
    }
}
