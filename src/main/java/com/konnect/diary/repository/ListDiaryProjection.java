package com.konnect.diary.repository;

public interface ListDiaryProjection {
    Long getDiaryId();

    String getTitle();

    String getStatus();

    Long getAreaId();

    String getAreaName();

    Long getLikeCount();

    String getStartDate();

    String getEndDate();
}
