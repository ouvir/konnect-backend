package com.konnect.diary.dto.response;

import com.konnect.diary.dto.request.AreaRequestDTO;
import com.konnect.tag.TagResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListDiaryResponseDTO {
    private Long diaryId;
    private String title;
    private String status;
    private String thumbnailImage;
    private AreaRequestDTO area;
    private Long likeCount;
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags;

    @Builder
    public ListDiaryResponseDTO(Long diaryId, String title, String status, String thumbnailImage, AreaRequestDTO area, Long likeCount, String startDate, String endDate, List<TagResponseDTO> tags) {
        this.diaryId = diaryId;
        this.title = title;
        this.status = status;
        this.thumbnailImage = thumbnailImage;
        this.area = area;
        this.likeCount = likeCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
    }
}
