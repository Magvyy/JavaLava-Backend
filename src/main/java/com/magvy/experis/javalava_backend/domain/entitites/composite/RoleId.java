package com.magvy.experis.javalava_backend.domain.entitites.composite;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
public class RoleId implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", nullable = false)
    private RoleEnum role;

    RoleId(){};
    public RoleId(Long userId, RoleEnum role) {
        this.userId = userId;
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
