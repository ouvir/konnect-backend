package com.konnect.route.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "루트 재정렬 + 거리 정보 요청")
public record RouteReorderRequest(

        @NotEmpty
        @Schema(description = "재정렬된 Route PK 리스트",
                example = "[12, 15, 10, 11]")
        List<Long> routeIds,

        @NotEmpty          // routeIds.size() - 1 과 맞는지 여부는 서비스 레벨에서 검증
        @Schema(description = "각 구간 거리(km) 리스트 — 마지막 루트는 거리 없음",
                example = "[123.1, 15.1, 10.5]")
        List<Double> distances
) { }
