package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "루트 조회 응답")
public record RouteResponse(

        @Schema(description = "루트 PK", example = "10")
        Long id,

        @Schema(description = "다이어리 내 방문 순서", example = "1")
        Integer orderIdx,

        @Schema(description = "방문 일시(ISO-8601)", example = "2025-06-15T10:30:00")
        LocalDateTime visitedAt,

        @Schema(description = "관광지 PK", example = "123")
        Integer attractionNo,

        @Schema(description = "관광지 이름", example = "남산 서울타워")
        String attractionTitle
) { }
