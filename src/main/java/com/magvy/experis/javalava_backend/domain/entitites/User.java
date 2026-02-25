package com.magvy.experis.javalava_backend.domain.entitites;

import com.magvy.experis.javalava_backend.application.security.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id", nullable = true)
    private Attachment attachment;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    public void addRole(RoleEnum role) {
        this.roles.add(new Role(this, role));
    }

    public void removeRole(RoleEnum role) {
        this.roles.remove(new Role(this, role));
    }

    // Used for testing
    public User(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = new ArrayList<>();
    }

    // Used for registering
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.roles = new ArrayList<>();
        this.roles.add(new Role(this, RoleEnum.USER));
    }

    public User() {

    }
}
