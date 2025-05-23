package com.konnect.user.repository;

import com.konnect.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findById(long id);
    UserEntity findByOauthCode(String oauthCode);
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
}
