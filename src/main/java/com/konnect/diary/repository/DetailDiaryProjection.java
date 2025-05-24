package com.konnect.diary.repository;

import com.konnect.comment.dto.CommentDto;
import com.konnect.tag.TagEntity;

import java.util.ArrayList;

public interface DetailDiaryProjection {
    Long getDiaryId();
    String getTitle();
    String getContent();
    boolean getIsUserLiked();
    Integer getLikeCount();
    String getStartDate();
    String getEndDate();
    ArrayList<TagEntity> getTags();
    ArrayList<CommentDto> getComments();
}
