package com.konnect.route.dto;

import com.konnect.attraction.dto.AttractionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "루트 + 관광지 상세 응답")
public record RouteDetailResponse(
        @Schema(description = "루트 PK", example = "10")
        Long id,

        @Schema(description = "방문 일자", example = "2025-05-23")
        String visitedDate,

        @Schema(description = "방문 시각", example = "09:30")
        String visitedTime,

        @Schema(description = "다음 경로까지의 거리", example = "13872")
        Double distance,

        @Schema(description = "관광지 상세 정보")
        AttractionDTO attraction
) { }
