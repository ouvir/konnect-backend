package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "루트 상세 응답")
public record RouteDetailResponse(
        Long id,
        String visitedDate,
        String visitedTime,
        Double distance,
        String title,
        Double latitude,
        Double longitude
) { }