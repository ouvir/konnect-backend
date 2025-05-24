package com.konnect.diary.repository;

import com.konnect.diary.entity.DiaryEntity;
import com.konnect.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    @Query(value = """
        SELECT d.diary_id AS diaryId, d.title AS title, d.area_id AS areaId, a.name AS areaName, COALESCE(l.cnt,0) AS likeCount, d.start_date AS startDate, d.end_date AS endDate, d.status
        FROM diaries d
        JOIN areas a ON a.area_id = d.area_id
        LEFT JOIN ( 
                SELECT diary_id, COUNT(*) AS cnt FROM likes WHERE is_deleted = FALSE GROUP BY diary_id
        ) l ON l.diary_id = d.diary_id
        WHERE d.area_id = :areaId AND d.status = 'published'
        """,
        countQuery = """
        SELECT COUNT(*)
        FROM diaries d
        WHERE d.area_id = :areaId
          AND d.status   = 'published'
        """,
        nativeQuery = true
    )
    Page<ListDiaryProjection> findDiariesByArea(
            @Param("areaId") Long areaId,
            Pageable pageable
    );

    @Query(value = """
        SELECT d.diary_id AS diaryId, d.title AS title, d.area_id AS areaId, a.name AS areaName, COALESCE(l.cnt,0) AS likeCount, d.start_date AS startDate, d.end_date AS endDate, d.status
        FROM diaries d
        JOIN areas a ON a.area_id = d.area_id
        LEFT JOIN (
                SELECT diary_id, COUNT(*) AS cnt FROM likes WHERE is_deleted = FALSE GROUP BY diary_id
        ) l ON l.diary_id = d.diary_id
        WHERE d.user_id = :userId
        """,
        countQuery = """
            SELECT COUNT(*)
            FROM diaries d
            WHERE d.user_id = :userId 
        """,
        nativeQuery = true
    )
    Page<ListDiaryProjection> fetchMyDiaries(@Param("userId") Long userId, Pageable pageable);

    @Query("""
      SELECT 
        d.diaryId                 AS diaryId,
        d.user.name          AS username,
        COUNT(l)          AS likeCount,
        d.title              AS title,
        d.content            AS content,
        FUNCTION('DATE_FORMAT', d.startDate, '%Y-%m-%d %H:%i:%s') AS startDate,
        FUNCTION('DATE_FORMAT', d.endDate,   '%Y-%m-%d %H:%i:%s') AS endDate,
        CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END   AS isUserLiked
      FROM DiaryEntity d
      LEFT JOIN LikeEntity l
        ON l.isDeleted = FALSE
        AND l.user.userId = :userId
        AND l.isDeleted = FALSE
      WHERE d.diaryId = :diaryId
      """)
    DetailDiaryProjection fetchDiaryDetail(
            @Param("diaryId") Long diaryId,
            @Param("userId")  Long userId
    );

    Long user(UserEntity user);
}