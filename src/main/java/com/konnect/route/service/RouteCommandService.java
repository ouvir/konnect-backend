package com.konnect.route.service;

import com.konnect.attraction.entity.Attraction;
import com.konnect.attraction.repository.AttractionRepository;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.route.dto.RouteAddAttractionRequest;
import com.konnect.route.dto.RouteDTO;
import com.konnect.route.entity.Route;
import com.konnect.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteCommandService {

    private final DiaryRepository diaryRepo;
    private final AttractionRepository attractionRepo;
    private final RouteRepository routeRepo;

    public RouteDTO addRoute(RouteAddAttractionRequest req) throws IllegalArgumentException {

        DiaryEntity diary = diaryRepo.findById(req.getDiaryId())
                .orElseThrow(() -> new IllegalArgumentException("다이어리 없음"));

        if(diary.getStatus().equals("published")) {
            throw new IllegalArgumentException("다이어리 수정 불가");
        }

        Attraction attraction = attractionRepo.findById(req.getAttractionNo())
                .orElseThrow(() -> new IllegalArgumentException("명소 없음"));

        Route route = Route.builder()
                .diary(diary)
                .visitedDate(req.getVisitedDate())
                .visitedTime("23:59")          // 고정
                .distance(null)               // null
                .title(attraction.getTitle())
                .latitude(attraction.getLatitude().doubleValue())
                .longitude(attraction.getLongitude().doubleValue())
                .build();

        Route saved = routeRepo.save(route);
        return RouteDTO.from(saved);          // DTO 변환 (필요 시 구현)
    }
}
