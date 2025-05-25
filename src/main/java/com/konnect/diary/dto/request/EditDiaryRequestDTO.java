package com.konnect.diary.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.konnect.util.CustomNullableString;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "드래프트 저장 요청 DTO")
public class EditDiaryRequestDTO {
    private Optional<Long> diaryId = Optional.empty();

    @NotBlank
    @Schema(description = "제목", example = "Summer Trip to Jeju", required = true)
    private String title;

    @Schema(description = "본문 내용", example = "제주도의 아름다운 해변...", required = false)
    private Optional<String> content = Optional.empty();

    @Schema(description = "지역 ID", example = "5", required = false)
    private Optional<Long> areaId = Optional.empty();

    @Schema(description = "태그 ID 리스트", example = "[101,102,103]", required = false)
    private List<Long> tags = new ArrayList<>();

    @Schema(description = "시작일 (yyyy-MM-dd)", example = "2025-06-01", required = true)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private Optional<String> startDate = Optional.empty();

    @Schema(description = "종료일 (yyyy-MM-dd)", example = "2025-06-05", required = true)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private Optional<String> endDate = Optional.empty();

    @Schema(description = "임시저장/게시 여부", example = "임시저장: editing / 게시: published")
    private String status;

    @ArraySchema(
            arraySchema = @Schema(
                    description = "하루 단위 여행 경로 리스트",
                    requiredMode = Schema.RequiredMode.REQUIRED
            ),
            schema = @Schema(implementation = DiaryRouteDTO.class)
    )
    private List<DiaryRouteDTO> routes = new ArrayList<>();

    /* ───────── Base64 이미지 ───────── */
    @Schema(description = "Base64 인코딩된 썸네일", example = "data:image/png;base64,iVBORw0K…")
    @JsonDeserialize(using = CustomNullableString.class)
    private String thumbnail;                // 단일

    @Schema(description = "Base64 인코딩된 본문 이미지 리스트")
    private List<String> images;          // 다중

    public boolean hasThumbnail() {
        return thumbnail != null && !thumbnail.isBlank();
    }
    public boolean hasImage() {
        return images != null && !images.isEmpty();
    }

    @Override
    public String toString() {
        return images.toString();
    }
}