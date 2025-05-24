package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "루트 재정렬 요청")
public record RouteReorderRequest(

        @NotEmpty
        @Schema(description = "정렬된 Route PK 리스트",
                example = "[12, 15, 10]")
        List<Long> routeIds,

        @NotEmpty
        @Schema(description = "각 루트의 방문 일자(yyyy-MM-dd) 리스트",
                example = "[\"2025-05-23\", \"2025-05-23\", \"2025-05-23\"]")
        List<String> visitedDates,

        @NotEmpty
        @Schema(description = "각 루트의 방문 시각(HH:mm) 리스트",
                example = "[\"09:00\", \"10:30\", \"12:00\"]")
        List<String> visitedTimes,

        @NotEmpty              // routeIds.size() - 1 과 길이가 같아야 함
        @Schema(description = "각 구간 거리 리스트 — 마지막 루트는 거리 없음",
                example = "[1200.5, 300.0]")
        List<Double> distances
) { }
