// com.konnect.tag.dto.TagDto
package com.konnect.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "태그 정보 DTO")
public class TagDto {

    @Schema(description = "태그 ID", example = "1")
    private Long id;

    @Schema(description = "태그 이름", example = "힐링")
    private String name;

    @Schema(description = "태그 영문 이름", example = "healing")
    private String nameEng;

    @Schema(description = "Prompt Tokens", example = "12333")
    private Integer promptToken;

    @Schema(description = "Completion Tokens", example = "12333")
    private Integer completionToken;

    @Schema(description = "Total Tokens", example = "12333")
    private Integer totalToken;
}