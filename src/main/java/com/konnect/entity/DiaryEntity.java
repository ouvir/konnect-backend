package com.konnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diaries")
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name="area_id")
    private Long areaId;

    private String title;

    private String content;

    @Column(name = "image_total_count")
    private Integer imageTotalCount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryTagEntity> tags = new ArrayList<>();

    // TODO: 여행 루트 컬럼 추가 예정

    @Builder
    public DiaryEntity(
            Long userId,
            Long areaId,
            String title,
            String content,
            Integer imageTotalCount,
            LocalDate startDate,
            LocalDate endDate,
            List<DiaryTagEntity> tags
    ) {
        this.userId = userId;
        this.areaId = areaId;
        this.title = title;
        this.content = content;
        this.imageTotalCount = imageTotalCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
    }
}
