package com.konnect.comment.dto;

import com.konnect.comment.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "댓글 및 대댓글 응답 DTO")
public class CommentDto {

    @Schema(description = "댓글 ID", example = "1")
    private Long commentId;

    @Schema(description = "작성자 ID", example = "10")
    private Long userId;

    @Schema(description = "연결된 일기 ID", example = "100")
    private Long diaryId;

    @Schema(description = "댓글 내용", example = "이 여행지 정말 좋네요!")
    private String content;

    @Schema(description = "작성 시각", example = "2025-05-22T14:33:00")
    private String createdAt;

    @Schema(description = "대댓글 목록", implementation = CommentDto.class)
    private List<CommentDto> children = new ArrayList<>();

    public static CommentDto from(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setUserId(comment.getUserId());
        dto.setDiaryId(comment.getDiaryId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}