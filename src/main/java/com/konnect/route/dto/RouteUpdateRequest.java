package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(description = "루트 수정 요청(부분 업데이트 지원)")
@Builder
public record RouteUpdateRequest(

        @Schema(description = "방문 일시(ISO-8601)", example = "2025-06-16T14:00:00")
        LocalDateTime visitedAt,

        @Schema(description = "새 방문 순서", example = "2")
        Integer orderIdx,

        @Schema(description = "변경할 관광지 PK", example = "321")
        Integer attractionNo
) { }
