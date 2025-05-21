package com.konnect.diary.repository;

import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiaryTagRepository extends JpaRepository<DiaryTagEntity, Long> {
    @Modifying
    @Query("DELETE FROM DiaryTagEntity dt WHERE dt.diary = :diary")
    void deleteByDiary(@Param("diary") DiaryEntity diary);
}
