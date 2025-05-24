package com.konnect.route.service;

import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.route.dto.*;
import com.konnect.route.entity.Route;
import com.konnect.route.repository.RouteRepository;
import com.konnect.util.SearchCondition;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 여행 경로(Route) CRUD 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final DiaryRepository diaryRepository;

    /* ---------- CREATE ---------- */

    public RouteDetailResponse create(RouteCreateRequest req, Long userId) {
        DiaryEntity diary = validateDiaryOwner(req.diaryId(), userId);

        Route saved = routeRepository.save(
                Route.builder()
                        .diary(diary)
                        .visitedDate(req.visitedDate())
                        .visitedTime(req.visitedTime())
                        .title(req.title())
                        .latitude(req.latitude())
                        .longitude(req.longitude())
                        .distance(req.distance())
                        .build()
        );

        return toDto(saved);
    }

    /* ---------- READ (LIST) ---------- */

    @Transactional(readOnly = true)
    public List<RouteDetailResponse> list(SearchCondition cond) {
        Long diaryId = Long.valueOf(cond.get("diaryId"));

        return routeRepository.findByDiaryDiaryIdOrderByVisitedDateAscVisitedTimeAsc(diaryId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /* ---------- UPDATE ---------- */

    public void update(Long id, RouteUpdateRequest req) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));

        /* 방문 일자·시간 */
        if (req.visitedDate() != null || req.visitedTime() != null) {
            String date = req.visitedDate() != null ? req.visitedDate() : route.getVisitedDate();
            String time = req.visitedTime() != null ? req.visitedTime() : route.getVisitedTime();
            route.updateVisited(date, time);
        }

        /* 위치 정보(제목·좌표) */
        if (req.title() != null || req.latitude() != null || req.longitude() != null) {
            String   title = req.title()     != null ? req.title()     : route.getTitle();
            Double   lat   = req.latitude()  != null ? req.latitude()  : route.getLatitude();
            Double   lon   = req.longitude() != null ? req.longitude() : route.getLongitude();
            route.updateLocation(title, lat, lon);
        }

        /* 다음 경로까지 거리 */
        if (req.distance() != null) {
            route.updateDistance(req.distance());
        }
    }

    /* ---------- DELETE ---------- */

    public void delete(Long id) {
        routeRepository.deleteById(id);
    }

    /* ---------- UTIL ---------- */

    /** 다이어리 소유자 검증 */
    private DiaryEntity validateDiaryOwner(Long diaryId, Long userId) {
        DiaryEntity diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Diary not found"));
        if (!diary.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인 다이어리가 아닙니다.");
        }
        return diary;
    }

    /** 엔티티 → DTO 변환 */
    private RouteDetailResponse toDto(Route r) {
        return new RouteDetailResponse(
                r.getId(),
                r.getVisitedDate(),
                r.getVisitedTime(),
                r.getDistance(),
                r.getTitle(),
                r.getLatitude(),
                r.getLongitude()
        );
    }
}
