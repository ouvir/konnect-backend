package com.konnect.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "여행 경로 항목 DTO")
public class DiaryRouteDetailDTO {
    @Schema(description = "명소 코드", example = "5001", required = true)
    private Integer attractionNo;

    @Schema(description = "방문 날짜 (yyyy-MM-dd)", example = "2025-06-01", required = true)
    private String visitedDate;

    @Schema(description = "방문 시간 (HH:mm:ss)", example = "09:30:00", required = true)
    private String visitedTime;

    @Schema(description = "이동 거리 (미터)", example = "1200.5", required = false)
    private Double distance;

    @Schema(description = "목적지 이름", example = "역삼 멀티캠퍼스")
    private String title;

    @Schema(description = "위도 (latitude)", example = "33.48901123456789012")
    private Double latitude;

    @Schema(description = "경도 (longitude)", example = "126.49830123456789012")
    private Double longitude;
}