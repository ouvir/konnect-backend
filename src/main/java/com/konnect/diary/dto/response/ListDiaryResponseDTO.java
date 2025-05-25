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
    private String thumbnail;
    private AreaRequestDTO area;
    private Long likeCount;
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags;
    private List<String> images;

    @Builder
    public ListDiaryResponseDTO(Long diaryId, String title, String status, String thumbnail, AreaRequestDTO area, Long likeCount, String startDate, String endDate, List<TagResponseDTO> tags, List<String> images) {
        this.diaryId = diaryId;
        this.title = title;
        this.status = status;
        this.thumbnail = thumbnail;
        this.area = area;
        this.likeCount = likeCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
        this.images = images;
    }
}
