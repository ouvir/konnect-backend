package com.konnect.repository;

import com.konnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByOauthCode(String oauthCode);
}
