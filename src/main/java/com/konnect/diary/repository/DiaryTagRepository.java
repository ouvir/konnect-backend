package com.konnect.diary.repository;

import com.konnect.diary.entity.DiaryTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryTagRepository extends JpaRepository<DiaryTagEntity, Long> {
}
