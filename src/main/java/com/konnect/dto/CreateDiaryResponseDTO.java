package com.konnect.dto;

import com.konnect.entity.DiaryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CreateDiaryResponseDTO {
    private Long diaryId;
    private Long userId;
    private String title;
    private String content;
    private Integer imageTotalCount;
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags;

    public static CreateDiaryResponseDTO from(DiaryEntity diary) {
        return new CreateDiaryResponseDTO(
                diary.getDiaryId(),
                diary.getUserId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getImageTotalCount(),
                diary.getStartDate(),
                diary.getEndDate(),
                new ArrayList<>()
        );
    }
}
