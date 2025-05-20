package com.konnect.route.repository;

import com.konnect.route.dto.RouteResponse;

import java.util.List;

public interface RouteRepositoryCustom {
    int nextOrderIdx(Long diaryId);
    List<RouteResponse> searchByDiary(Long diaryId);
}
