package com.konnect.diary.dto.response;

import com.konnect.diary.dto.request.DiaryRouteDTO;
import com.konnect.tag.TagResponseDTO;
import com.konnect.comment.dto.CommentDto;
import com.konnect.diary.repository.DetailDiaryProjection;
import com.konnect.user.dto.UserInfoDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailDiaryResponseDTO {
    private Long id;
    private String title;
    private String content;
    private UserInfoDTO userInfo;
    private boolean isUserLiked;
    private Integer likeCount;
    private String thumbnail;
    private List<String> images;
    private String startDate;
    private String endDate;
    private List<TagResponseDTO> tags = new ArrayList<>();
    private List<DiaryRouteDTO> routes = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();
}