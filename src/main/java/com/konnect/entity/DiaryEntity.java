package com.konnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private String startDate;

    @Column(name = "end_date")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private String endDate;

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
            String startDate,
            String endDate,
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
