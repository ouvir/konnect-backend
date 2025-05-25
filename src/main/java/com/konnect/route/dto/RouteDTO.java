package com.konnect.route.dto;

import com.konnect.route.entity.Route;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "여행 경로 응답 DTO")
public class RouteDTO {

    @Schema(description = "루트 PK", example = "10")
    private Long id;

    @Schema(description = "다이어리 ID", example = "5")
    private Long diaryId;

    @Schema(description = "방문 일자", example = "2025-06-01")
    private String visitedDate;

    @Schema(description = "방문 시각", example = "23:59")
    private String visitedTime;

    @Schema(description = "명소 제목", example = "경복궁")
    private String title;

    @Schema(description = "위도", example = "37.579617")
    private Double latitude;

    @Schema(description = "경도", example = "126.977041")
    private Double longitude;

    @Schema(description = "다음 명소까지 거리", example = "null", nullable = true)
    private Double distance;

    public static RouteDTO from(Route r) {
        return new RouteDTO(
                r.getId(),
                r.getDiary().getDiaryId(),
                r.getVisitedDate(),
                r.getVisitedTime(),
                r.getTitle(),
                r.getLatitude(),
                r.getLongitude(),
                r.getDistance()
        );
    }
}
