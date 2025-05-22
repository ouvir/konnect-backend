package com.konnect.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    private Long diaryId;
    private String content;
    private String createdAt;
}