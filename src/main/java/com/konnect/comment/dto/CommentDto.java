package com.konnect.comment.dto;

import com.konnect.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long userId;
    private Long diaryId;
    private String content;
    private String createdAt;
    private boolean isDeleted;
    private List<CommentDto> children = new ArrayList<>();

    public static CommentDto from(Comment entity) {
        return new CommentDto(
                entity.getCommentId(),
                entity.getUserId(),
                entity.getDiaryId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.isDeleted(),
                new ArrayList<>()
        );
    }
}