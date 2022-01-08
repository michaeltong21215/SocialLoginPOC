package com.login.social.socialLoginDemo.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;
    //Todo: need to understand the essence and applicability of provider id
    @Column(name="providerid")
    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    public User(String username, String email, String password, String providerId, AuthProvider authProvider){
        this.username = username;
        this.email = email;
        this.password = password;
        this.providerId = providerId;
        this.authProvider = authProvider;
    }
}
