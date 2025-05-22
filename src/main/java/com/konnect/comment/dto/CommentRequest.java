package com.konnect.comment.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private Long diaryId;
    private Long parentId; // 댓글이면 null, 대댓글이면 부모 ID
    private String content;
    private String createdAt; // 프론트에서 받는 시간
}