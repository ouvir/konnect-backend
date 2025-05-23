package com.konnect.route.repository;

import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.util.SearchCondition;
import java.util.List;
import java.util.Optional;

public interface RouteRepositoryCustom {

    List<RouteDetailResponse> searchByDiary(Long diaryId, SearchCondition condition);

    /** Route + Attraction 을 DTO 로 즉시 매핑 */
    Optional<RouteDetailResponse> findDetailById(Long routeId);
}