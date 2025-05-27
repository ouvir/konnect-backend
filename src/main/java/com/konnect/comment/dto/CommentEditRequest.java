package com.konnect.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentEditRequest {

    @Schema(description = "댓글 내용", example = "이 여행지 정말 좋네요!")
    private String content;
}
