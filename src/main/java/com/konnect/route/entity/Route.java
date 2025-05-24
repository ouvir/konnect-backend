package com.konnect.route.entity;

import com.konnect.attraction.entity.Attraction;
import com.konnect.diary.entity.DiaryEntity;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "routes")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "여행 경로(Route) 엔티티")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "루트 PK", example = "10")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no", nullable = false)
    @Schema(description = "관광지 FK", requiredMode = Schema.RequiredMode.REQUIRED)
    private Attraction attraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    @Schema(description = "다이어리 FK", requiredMode = Schema.RequiredMode.REQUIRED)
    private DiaryEntity diary;

    @Column(name = "visitedDate", length = 20, nullable = false)
    @Schema(description = "방문 일자(yyyy-MM-dd)", example = "2025-05-23")
    private String visitedDate;

    @Column(name = "visitedTime", length = 20, nullable = false)
    @Schema(description = "방문 시각(HH:mm)", example = "09:30")
    private String visitedTime;

    @Column(name = "distance")
    @Schema(description = "다음 경로까지의 거리", example = "13872")
    private Double distance;

    // ------ Mutators ------
    public void changeAttraction(Attraction attraction) { this.attraction = attraction; }
    public void updateVisitedDate(String date)        { this.visitedDate = date;    }
    public void updateVisitedTime(String time)        { this.visitedTime = time;    }
    public void updateDistance(Double distance)       { this.distance = distance;   }
}