package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Role> roles;

    public void addRole(RoleEnum role) {
        this.roles.add(new Role(this, role));
    }

    public void removeRole(RoleEnum role) {
        this.roles.remove(new Role(this, role));
    }

    public User(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = new ArrayList<>();
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.roles = new ArrayList<>();
        this.addRole(RoleEnum.USER);
    }

    public User() {

    }
}
