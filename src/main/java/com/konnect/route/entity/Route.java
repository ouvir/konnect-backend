package com.konnect.route.entity;

import com.konnect.attraction.entity.Attraction;
import com.konnect.diary.entity.DiaryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "routes",
        uniqueConstraints = @UniqueConstraint(name = "uk_diary_idx", columnNames = {"diary_id", "idx"}))
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no", nullable = false)
    private Attraction attraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private DiaryEntity diary;

    @Column(name = "idx", nullable = false)
    private Integer orderIdx;

    @Column(name = "date", nullable = false)
    private Integer visitedAt;

    @Column(name = "distance")
    private Double distance;

    public void updateOrder(int newOrder) {
        this.orderIdx = newOrder;
    }

    public void updateVisitedAt(Integer date) {
        this.visitedAt = date;
    }

    public void changeAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public void updateDistance(Double distance) {
        this.distance = distance;
    }
}
