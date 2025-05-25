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
        SELECT d.diary_id AS diaryId, d.title AS title, d.area_id AS areaId, a.name AS areaName, a.name_eng AS areaNameEng COALESCE(l.cnt,0) AS likeCount, d.start_date AS startDate, d.end_date AS endDate, d.status
        FROM diaries d
        JOIN areas a ON a.area_id = d.area_id
        LEFT JOIN ( 
                SELECT diary_id, COUNT(*) AS cnt 
                FROM likes 
                WHERE is_deleted = FALSE 
                GROUP BY diary_id
        ) l ON l.diary_id = d.diary_id
        WHERE d.area_id = :areaId 
           AND d.status = 'published'
           AND d.is_deleted = FALSE
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
        SELECT d.diary_id AS diaryId, d.title AS title, d.area_id AS areaId, a.name AS areaName, a.name_eng AS areaNameEng, COALESCE(l.cnt,0) AS likeCount, d.start_date AS startDate, d.end_date AS endDate, d.status
        FROM diaries d
        JOIN areas a ON a.area_id = d.area_id
        LEFT JOIN (
                SELECT diary_id, COUNT(*) AS cnt 
                FROM likes
                WHERE is_deleted = FALSE
                GROUP BY diary_id
        ) l ON l.diary_id = d.diary_id
        WHERE d.user_id = :userId
            AND d.is_deleted = FALSE
        """,
        countQuery = """
            SELECT COUNT(*)
            FROM diaries d
            WHERE d.user_id = :userId
        """,
        nativeQuery = true
    )
    Page<ListDiaryProjection> fetchMyDiaries(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
        SELECT 
          d.diary_id      AS diaryId,
          COALESCE(l.cnt,0) AS likeCount,
          d.title         AS title,
          d.content       AS content,
          d.area_id       AS areaId,
          a.name          AS areaName,
          a.name_eng      AS areaNameEng,
          DATE_FORMAT(d.start_date, '%Y-%m-%d %H:%i:%s') AS startDate,
          DATE_FORMAT(d.end_date,   '%Y-%m-%d %H:%i:%s') AS endDate,
          EXISTS (
            SELECT 1
            FROM likes lx
            WHERE lx.diary_id = d.diary_id
                AND lx.user_id    = :userId
                AND lx.is_deleted = FALSE
            ) AS isUserLiked
        FROM diaries d
          JOIN users  u ON d.user_id = u.user_id
          JOIN areas  a ON d.area_id = a.area_id
          LEFT JOIN (
            SELECT diary_id, COUNT(*) AS cnt
            FROM likes
            WHERE is_deleted = FALSE
            GROUP BY diary_id
          ) l ON l.diary_id = d.diary_id
        WHERE d.diary_id = :diaryId
          AND d.is_deleted = FALSE
        """,
        nativeQuery = true
    )
    DetailDiaryProjection fetchDiaryDetail(
            @Param("diaryId") Long diaryId,
            @Param("userId")  Long userId
    );

    Long user(UserEntity user);
}