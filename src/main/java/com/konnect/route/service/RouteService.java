package com.konnect.route.service;

import com.konnect.attraction.entity.Attraction;
import com.konnect.attraction.repository.AttractionRepository;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.route.dto.RouteCreateByDateRequest;
import com.konnect.route.dto.RouteCreateRequest;
import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.route.dto.RouteUpdateRequest;
import com.konnect.route.entity.Route;
import com.konnect.route.repository.RouteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final DiaryRepository diaryRepository;
    private final AttractionRepository attractionRepository;

    public Long create(RouteCreateRequest dto, Long userId) {

        DiaryEntity diary = validDiaryOfUser(dto.diaryId(), userId);

        Attraction attraction = attractionRepository.findById(dto.attractionNo())
                .orElseThrow(() -> new EntityNotFoundException("Attraction not found"));

        int order = dto.orderIdx() != null ? dto.orderIdx() : routeRepository.nextOrderIdx(diary.getDiaryId());

        Route route = Route.builder()
                .diary(diary)
                .attraction(attraction)
                .orderIdx(order)
                .visitedAt(dto.visitedAt())
                .build();

        return routeRepository.save(route).getId();
    }

    /** READ (다이어리별) */
    @Transactional(readOnly = true)
    public List<RouteDetailResponse> listByDiary(Long diaryId) {
        return routeRepository.searchByDiary(diaryId);
    }

    /** UPDATE */
    public void update(Long id, RouteUpdateRequest dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found"));

        if (dto.visitedAt() != null) route.updateVisitedAt(dto.visitedAt());
        if (dto.orderIdx() != null)  route.updateOrder(dto.orderIdx());
        if (dto.attractionNo() != null) {
            Attraction attraction = attractionRepository.findById(dto.attractionNo())
                    .orElseThrow(() -> new EntityNotFoundException("Attraction not found"));
            route.changeAttraction(attraction);
        }
    }

    /** DELETE */
    public void delete(Long id) {
        routeRepository.deleteById(id);
    }


    @Transactional
    public Long createByDate(RouteCreateByDateRequest dto, Long userId) {
        // (다이어리 주인 검증 로직 동일)
        DiaryEntity diary = validDiaryOfUser(dto.diaryId(), userId);

        Attraction attraction = attractionRepository.findById(dto.attractionNo())
                .orElseThrow(() -> new EntityNotFoundException("Attraction not found"));

        int order = dto.orderIdx() != null
                ? dto.orderIdx()
                : routeRepository.nextOrderIdx(diary.getDiaryId());

        LocalDateTime visitedAt = dto.visitedDate().atTime(23, 59, 59);

        Route route = Route.builder()
                .diary(diary)
                .attraction(attraction)
                .orderIdx(order)
                .visitedAt(visitedAt)
                .build();

        return routeRepository.save(route).getId();
    }

    @Transactional(readOnly = true)
    public List<RouteDetailResponse> listByDiaryAndDate(Long diaryId, LocalDate date) {
        return routeRepository.searchByDiaryAndDate(diaryId, date);
    }

    /* 공통 유틸 */
    private DiaryEntity validDiaryOfUser(Long diaryId, Long userId) {
        DiaryEntity diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Diary not found"));
        if (!diary.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인 다이어리가 아닙니다.");
        }
        return diary;
    }
}
