package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "루트 생성 요청")
@Builder
public record RouteCreateRequest(

        @NotNull
        @Schema(description = "관광지 PK (attractions.no)", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer attractionNo,

        @NotNull
        @Schema(description = "다이어리 PK", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        Long diaryId,

        @NotNull
        @Schema(description = "방문 날짜 인덱스", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer visitedAt,

        @Schema(description = "방문날짜 기반 방문 순서(생략 시 서버가 자동 채번)", example = "1")
        Integer orderIdx
) { }
