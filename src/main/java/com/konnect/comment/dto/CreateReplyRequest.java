package com.konnect.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReplyRequest {
    private Long diaryId;
    private Long parentId;
    private String content;
    private String createdAt;
}