package com.konnect.diary.dto.response;

import com.konnect.diary.dto.request.DiaryRouteDTO;
import com.konnect.tag.TagResponseDTO;
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
public class DetailDiaryResponseDTO {
    private Long id;
    private String title;
    private String content;
    private boolean isUserLiked;
    private Integer likeCount;
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags = new ArrayList<>();
    private List<DiaryRouteDTO> routes = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();

    public static DetailDiaryResponseDTO from(
            DetailDiaryProjection projection,
            List<TagResponseDTO> tags,
            List<DiaryRouteDTO> routes,
            List<CommentDto> comments
    ) {
        DetailDiaryResponseDTO detailDiaryResponseDTO = new DetailDiaryResponseDTO();
        detailDiaryResponseDTO.setId(projection.getDiaryId());
        detailDiaryResponseDTO.setTitle(projection.getTitle());
        detailDiaryResponseDTO.setContent(projection.getContent());
        detailDiaryResponseDTO.setUserLiked(projection.getIsUserLiked());
        detailDiaryResponseDTO.setLikeCount(projection.getLikeCount());
        detailDiaryResponseDTO.setStartDate(projection.getStartDate());
        detailDiaryResponseDTO.setEndDate(projection.getEndDate());
        detailDiaryResponseDTO.setTags(tags);
        detailDiaryResponseDTO.setRoutes(routes);
        detailDiaryResponseDTO.setComments(comments);
        return detailDiaryResponseDTO;
    }
}