package com.konnect.repository;

import com.konnect.entity.DiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
}