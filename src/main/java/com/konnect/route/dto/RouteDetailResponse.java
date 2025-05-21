package com.konnect.route.dto;

import com.konnect.attraction.dto.AttractionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "루트 + 관광지 상세 응답")
public record RouteDetailResponse(

        @Schema(description = "루트 PK", example = "10")
        Long id,

        @Schema(description = "다이어리 내 방문 순서", example = "1")
        Integer orderIdx,

        @Schema(description = "방문 일시(ISO-8601)", example = "2025-06-15T10:30:00")
        LocalDateTime visitedAt,

        @Schema(description = "관광지 상세 정보")
        AttractionDTO attraction
) { }
