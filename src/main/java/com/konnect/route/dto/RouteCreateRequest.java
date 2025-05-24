package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "루트 생성 요청")
public record RouteCreateRequest(
        @NotNull Long diaryId,
        @NotNull String visitedDate,
        @NotNull String visitedTime,
        @NotNull String title,
        @NotNull Double latitude,
        @NotNull Double longitude,
        Double distance
) { }