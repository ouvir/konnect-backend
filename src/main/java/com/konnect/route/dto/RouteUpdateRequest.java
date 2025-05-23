package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "루트 수정 요청(부분 업데이트 지원)")
@Builder
public record RouteUpdateRequest(
        @Schema(description = "방문 일자", example = "2025-05-24")
        String visitedDate,
        @Schema(description = "방문 시각", example = "14:10")
        String visitedTime,
        @Schema(description = "제목", example = "14:10")
        String title,
        @Schema(description = "위도", example = "14:10")
        Double latitude,
        @Schema(description = "경도", example = "14:10")
        Double longitude,
        @Schema(description = "다음 경로까지의 거리", example = "13872")
        Double distance
) { }