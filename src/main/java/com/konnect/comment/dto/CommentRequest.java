package com.konnect.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private Long diaryId;
    private Long parentId;
    private String content;
    private String createdAt;
}