package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "루트 생성 요청")
@Builder
public record RouteCreateRequest(

        @NotNull
        @Schema(description = "관광지 PK (attractions.no)", example = "54446", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer attractionNo,

        @NotNull
        @Schema(description = "다이어리 PK", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        Long diaryId,

        @NotNull
        @Schema(description = "방문 일자(yyyy-MM-dd)", example = "2025-05-23", requiredMode = Schema.RequiredMode.REQUIRED)
        String visitedDate,

        @NotNull
        @Schema(description = "방문 시각(HH:mm)", example = "09:30", requiredMode = Schema.RequiredMode.REQUIRED)
        String visitedTime,

        @Schema(description = "다음 경로까지의 거리", example = "13872")
        Double distance
) { }