package com.konnect.comment.dto;

import com.konnect.comment.Comment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentDto {
    private Long commentId;
    private Long userId;
    private Long diaryId;
    private String content;
    private String createdAt;
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