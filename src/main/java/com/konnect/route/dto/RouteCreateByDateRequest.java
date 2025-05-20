package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Schema(description = "루트 생성 요청(방문 날짜만 제공)")
@Builder
public record RouteCreateByDateRequest(
        @NotNull
        @Schema(description = "관광지 PK", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer attractionNo,

        @NotNull
        @Schema(description = "다이어리 PK", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
        Long diaryId,

        @NotNull
        @Schema(description = "방문 날짜(yyyy-MM-dd, 시간 미포함)", example = "2025-06-15",
                requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDate visitedDate,

        @Schema(description = "다이어리 내 방문 순서(생략 시 자동)", example = "3")
        Integer orderIdx
) { }
