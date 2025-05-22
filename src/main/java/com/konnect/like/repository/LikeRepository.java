package com.konnect.like.repository;

import com.konnect.like.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    LikeEntity findByDiary_DiaryIdAndUser_UserId(Long userId, Long diaryId);
}
