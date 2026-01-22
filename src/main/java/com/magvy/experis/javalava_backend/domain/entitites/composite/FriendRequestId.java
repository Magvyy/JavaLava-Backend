package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.domain.entitites.User;

import java.io.Serializable;

public class FriendRequestId implements Serializable {
    private User from;
    private User to;

    public FriendRequestId(User from, User to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        FriendRequestId that = (FriendRequestId) o;
        return this.from.getId() == that.from.getId() && this.to.getId() == that.to.getId();
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }
}
