package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "루트 수정 요청(부분 업데이트 지원)")
@Builder
public record RouteUpdateRequest(

        @Schema(description = "방문 날짜 인덱스", example = "1")
        Integer visitedAt,

        @Schema(description = "새 방문 순서", example = "2")
        Integer orderIdx,

        @Schema(description = "변경할 관광지 PK", example = "321")
        Integer attractionNo,

        @Schema(description = "다음 경로까지의 거리", example = "13872")
        Double distance
) { }
