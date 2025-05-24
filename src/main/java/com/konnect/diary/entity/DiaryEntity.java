package com.konnect.diary.entity;

import com.konnect.diary.dto.CreateDiaryDraftRequestDTO;
import com.konnect.entity.AreaEntity;
import com.konnect.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diaries")
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="area_id")
    private AreaEntity area;

    private String title;

    private String content;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryTagEntity> tags = new ArrayList<>();

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
    private String createdAt;

    // TODO: 여행 루트 컬럼 추가 예정

}
