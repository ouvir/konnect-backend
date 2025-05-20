package com.konnect.diary.dto;

import com.konnect.auth.dto.TagResponseDTO;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags;

    private List<String> imageCodes;

    public static CreateDiaryResponseDTO from(DiaryEntity diary, List<String> imageCodes) {
        List<TagResponseDTO> tagResponses = new ArrayList<>();
        for (DiaryTagEntity diaryTag : diary.getTags()) {
            tagResponses.add(TagResponseDTO.from(diaryTag.getTag()));
        }

        return new CreateDiaryResponseDTO(
                diary.getDiaryId(),
                diary.getUser().getUserId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getStartDate(),
                diary.getEndDate(),
                tagResponses,
                imageCodes
        );
    }
}
