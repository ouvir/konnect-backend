package com.konnect.diary.dto.response;

import com.konnect.diary.dto.request.DiaryRouteDTO;
import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.tag.TagResponseDTO;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @ArraySchema(
            arraySchema = @Schema(
                    description = "하루 단위 여행 경로 리스트 (day별)",
                    requiredMode = Schema.RequiredMode.REQUIRED
            ),
            schema = @Schema(implementation = DiaryRouteDTO.class)
    )
    private List<DiaryRouteDTO> routes;

    private String thumbnailImage;
    private List<String> images;

    public static CreateDiaryResponseDTO from(DiaryEntity diary, String thumbnailImage, List<String> images, List<DiaryRouteDTO> routes) {
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
                routes,
                thumbnailImage,
                images
        );
    }
}
