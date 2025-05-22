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

    /**
     * 루트 생성 + 상세 DTO 반환
     */
    @Transactional
    public RouteDetailResponse create(RouteCreateRequest dto, Long userId) {

        // 1. 본인 소유 다이어리 검증
        DiaryEntity diary = validDiaryOfUser(dto.diaryId(), userId);

        // 2. orderIdx 자동 할당
        int order = (dto.orderIdx() != null)
                ? dto.orderIdx()
                : routeRepository.nextOrderIdx(diary.getDiaryId(), dto.visitedAt());   // QueryDSL 커스텀

        Attraction attraction = attractionRepository.findById(dto.attractionNo())
                .orElseThrow(() -> new EntityNotFoundException("Attraction not found"));

        // 3. 엔티티 생성·저장
        Route saved = routeRepository.save(
                Route.builder()
                        .diary(diary)
                        .attraction(attraction)   // FK만 지정 (join 시 사용)
                        .orderIdx(order)
                        .visitedAt(dto.visitedAt())
                        .build()
        );

        // 4. Route + Attraction join → RouteDetailResponse 로 즉시 매핑
        return routeRepository.findDetailById(saved.getId())          // QueryDSL 커스텀
                .orElseThrow(() -> new IllegalStateException("방금 저장한 루트를 찾을 수 없습니다."));
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

    @Transactional(readOnly = true)
    public List<RouteDetailResponse> listByDiaryAndDate(Long diaryId, Integer date) {
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
