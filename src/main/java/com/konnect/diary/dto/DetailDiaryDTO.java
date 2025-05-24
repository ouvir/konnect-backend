package com.konnect.diary.dto;

import com.konnect.auth.dto.TagResponseDTO;
import com.konnect.comment.dto.CommentDto;
import com.konnect.diary.repository.DetailDiaryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailDiaryDTO {
    private Long id;
    private String title;
    private String content;
    private boolean isUserLiked;
    private Integer likeCount;
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();

    public static DetailDiaryDTO from(
            DetailDiaryProjection projection,
            List<TagResponseDTO> tags,
            List<CommentDto> comments
    ) {
        DetailDiaryDTO detailDiaryDTO = new DetailDiaryDTO();
        detailDiaryDTO.setId(projection.getDiaryId());
        detailDiaryDTO.setTitle(projection.getDiaryTitle());
        detailDiaryDTO.setContent(projection.getDiaryContent());
        detailDiaryDTO.setUserLiked(projection.getIsUserLiked());
        detailDiaryDTO.setLikeCount(projection.getLikeCount());
        detailDiaryDTO.setStartDate(projection.getStartDate());
        detailDiaryDTO.setEndDate(projection.getEndDate());
        detailDiaryDTO.setTags(tags);
        detailDiaryDTO.setComments(comments);
        return detailDiaryDTO;
    }
}
