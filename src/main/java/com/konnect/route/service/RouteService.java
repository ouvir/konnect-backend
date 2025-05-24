package com.konnect.route.service;

import com.konnect.attraction.entity.Attraction;
import com.konnect.attraction.repository.AttractionRepository;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.route.dto.RouteCreateRequest;
import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.route.dto.RouteUpdateRequest;
import com.konnect.route.entity.Route;
import com.konnect.route.repository.RouteRepository;
import com.konnect.util.SearchCondition;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final DiaryRepository diaryRepository;
    private final AttractionRepository attractionRepository;

    /* ---------- CREATE ---------- */
    public RouteDetailResponse create(RouteCreateRequest dto, Long userId) {

        DiaryEntity diary = validDiaryOfUser(dto.diaryId(), userId);

        Attraction attraction = attractionRepository.findById(dto.attractionNo())
                .orElseThrow(() -> new EntityNotFoundException("Attraction not found"));

        Route saved = routeRepository.save(
                Route.builder()
                        .diary(diary)
                        .attraction(attraction)
                        .visitedDate(dto.visitedDate())
                        .visitedTime(dto.visitedTime())
                        .distance(dto.distance())
                        .build()
        );

        return routeRepository.findDetailById(saved.getId())
                .orElseThrow(() -> new IllegalStateException("방금 저장한 루트를 찾을 수 없습니다."));
    }

    /* ---------- LIST ---------- */
    @Transactional(readOnly = true)
    public List<RouteDetailResponse> list(SearchCondition cond) {
        Long diaryId = Long.valueOf(cond.get("diaryId"));
        return routeRepository.searchByDiary(diaryId, cond);
    }

    /* ---------- UPDATE ---------- */
    public void update(Long id, RouteUpdateRequest dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));

        if (dto.visitedDate() != null)  route.updateVisitedDate(dto.visitedDate());
        if (dto.visitedTime() != null)  route.updateVisitedTime(dto.visitedTime());

        if (dto.attractionNo() != null) {
            Attraction attraction = attractionRepository.findById(dto.attractionNo())
                    .orElseThrow(() -> new EntityNotFoundException("Attraction not found"));
            route.changeAttraction(attraction);
        }
        if (dto.distance() != null)     route.updateDistance(dto.distance());
    }

    /* ---------- DELETE ---------- */
    public void delete(Long id) {
        routeRepository.deleteById(id);
    }

    /* ---------- 유틸 ---------- */
    private DiaryEntity validDiaryOfUser(Long diaryId, Long userId) {
        DiaryEntity diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Diary not found"));
        if (!diary.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인 다이어리가 아닙니다.");
        }
        return diary;
    }
}