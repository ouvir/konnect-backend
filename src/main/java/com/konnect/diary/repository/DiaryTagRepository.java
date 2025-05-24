package com.konnect.diary.repository;

import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import com.konnect.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryTagRepository extends JpaRepository<DiaryTagEntity, Long> {
    @Modifying
    @Query("DELETE FROM DiaryTagEntity dt WHERE dt.diary = :diary")
    void deleteByDiary(@Param("diary") DiaryEntity diary);

    @Query("""
      SELECT dt.tag
      FROM DiaryTagEntity dt
      WHERE dt.diary.diaryId = :diaryId
      ORDER BY dt.id ASC
    """)
    List<TagEntity> findTop3ByDiary_DiaryIdOrderByIdAsc(Long diaryId);
}