package com.konnect.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "AI 태그 생성 요청 DTO")
public class GenerateTagRequestDTO {

    @NotBlank
    @Schema(description = "제목 또는 본문 내용 전체 문자열", example = "힐링이 필요한 여행이었다. 산책도 하고 사진도 찍었다.")
    private String content;
}