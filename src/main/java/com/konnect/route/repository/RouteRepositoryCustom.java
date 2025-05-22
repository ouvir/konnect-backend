package com.konnect.route.repository;

import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.util.SearchCondition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RouteRepositoryCustom {
    int nextOrderIdx(Long diaryId, Integer visitedAt);
    List<RouteDetailResponse> searchByDiary(Long diaryId, SearchCondition condition);
    Optional<RouteDetailResponse> findDetailById(Long routeId);
}
