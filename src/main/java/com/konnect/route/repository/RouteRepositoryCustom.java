package com.konnect.route.repository;

import com.konnect.route.dto.RouteDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface RouteRepositoryCustom {
    int nextOrderIdx(Long diaryId);
    List<RouteDetailResponse> searchByDiary(Long diaryId);
    List<RouteDetailResponse> searchByDiaryAndDate(Long diaryId, LocalDate date);
}
