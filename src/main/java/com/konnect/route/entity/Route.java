package com.konnect.route.entity;

import com.konnect.diary.entity.DiaryEntity;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "routes")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "여행 경로(Route) 엔티티 – 명소 정보 내장")
public class Route {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "PK", example = "10")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    @Schema(description = "다이어리 FK")
    private DiaryEntity diary;

    @Column(length = 20, nullable = false)
    @Schema(description = "방문 일자", example = "2025-05-23")
    private String visitedDate;

    @Column(length = 20, nullable = false)
    @Schema(description = "방문 시각", example = "09:30")
    private String visitedTime;

    @Schema(description = "다음 경로까지 거리", example = "13872")
    private Double distance;

    @Column(length = 500, nullable = false)
    @Schema(description = "명소 제목", example = "경복궁")
    private String title;

    @Column(nullable = false)
    @Schema(description = "위도", example = "37.5796170")
    private Double latitude;

    @Column(nullable = false)
    @Schema(description = "경도", example = "126.9770410")
    private Double longitude;

    /* --- Mutators --- */
    public void updateVisited(String date, String time){ this.visitedDate=date; this.visitedTime=time; }
    public void updateDistance(Double d){ this.distance=d; }
    public void updateLocation(String title, Double lat, Double lng){ this.title=title; this.latitude=lat; this.longitude=lng; }
}