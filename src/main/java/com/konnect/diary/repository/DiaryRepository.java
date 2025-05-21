package com.konnect.diary.repository;

import com.konnect.diary.dto.ListDiaryResponseDTO;
import com.konnect.diary.entity.DiaryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    @Query(
            value = """
        SELECT
          d.diary_id      AS diaryId,
          d.title         AS title,
          d.area_id       AS areaId,
          a.name          AS areaName,
          COALESCE(l.cnt,0) AS likeCount,
          d.start_date    AS startDate,
          d.end_date      AS endDate
        FROM diaries d
        JOIN areas a
          ON a.area_id = d.area_id
        LEFT JOIN (
          SELECT diary_id, COUNT(*) AS cnt
          FROM likes
          GROUP BY diary_id
        ) l
          ON l.diary_id = d.diary_id
        WHERE d.area_id = :areaId          -- 파라미터 바인딩
          AND d.status   = 'published'
        GROUP BY
          d.diary_id,
          d.title,
          d.area_id,
          a.name,
          d.start_date,
          d.end_date
        """,
            nativeQuery = true
    )
    List<ListDiaryProjection> findDiariesByArea(
            @Param("areaId") Long areaId,
            Pageable pageable
    );
}