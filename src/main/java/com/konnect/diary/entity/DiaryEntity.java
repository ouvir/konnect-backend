package com.konnect.diary.entity;

import com.konnect.entity.AreaEntity;
import com.konnect.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diaries")
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="area_id")
    private AreaEntity area;

    private String title;

    private String content;
//TODO
//    @Column(name = "image_total_count")
//    private Integer imageTotalCount;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryTagEntity> tags = new ArrayList<>();

    private String status;

    // TODO: 여행 루트 컬럼 추가 예정

    @Builder
    public DiaryEntity(
            UserEntity user,
            AreaEntity area,
            String title,
            String content,
//            Integer imageTotalCount,
            String startDate,
            String endDate,
            List<DiaryTagEntity> tags,
            String status
    ) {
        this.user = user;
        this.area = area;
        this.title = title;
        this.content = content;
//        this.imageTotalCount = imageTotalCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
        this.status = status;
    }
}
