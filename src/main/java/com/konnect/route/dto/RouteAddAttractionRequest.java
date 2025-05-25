package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "명소 기반 루트 추가 요청 DTO")
public class RouteAddAttractionRequest {

    @Schema(description = "다이어리 ID", example = "5", required = true)
    private Long diaryId;

    @Schema(description = "명소 NO (attraction.no)", example = "56644", required = true)
    private Integer attractionNo;

    @Schema(description = "방문 날짜 (YYYY-MM-DD)", example = "2025-06-01", required = true)
    private String visitedDate;
}