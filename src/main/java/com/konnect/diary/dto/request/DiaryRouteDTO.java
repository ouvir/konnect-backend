package com.konnect.diary.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "하루 단위 여행 경로 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRouteDTO {
    @Schema(description = "여행 날짜 (yyyy-MM-dd)", example = "2025-06-01", required = true)
    private String date;

    @ArraySchema(
            arraySchema = @Schema(
                    description = "해당 날짜의 경로 상세 리스트",
                    requiredMode = Schema.RequiredMode.REQUIRED
            ),
            schema = @Schema(implementation = DiaryRouteDetailDTO.class)
    )
    private List<DiaryRouteDetailDTO> items;
}