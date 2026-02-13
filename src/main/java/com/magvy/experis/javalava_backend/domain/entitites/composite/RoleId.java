package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import com.magvy.experis.javalava_backend.domain.entitites.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
public class RoleId implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

    @Column(name = "user_id")
    private Long userId;

    public RoleId() {

    }

    public RoleId(User user, RoleEnum role) {
        this.userId = user.getId();
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        RoleId that = (RoleId) o;
        return this.userId.equals(that.userId) && this.role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return userId.hashCode() + role.hashCode();
    }
}
