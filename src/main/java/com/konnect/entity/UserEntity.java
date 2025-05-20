package com.konnect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "oauth_code")
    private String oauthCode;

    @Builder
    public UserEntity(String name, String email, String password, String role, String oauthCode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.oauthCode = oauthCode;
    }

}
