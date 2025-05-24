package com.konnect.diary.repository;

import com.konnect.comment.dto.CommentDto;
import com.konnect.entity.TagEntity;

import java.util.ArrayList;

public interface DetailDiaryProjection {
    Long getDiaryId();
    String getDiaryTitle();
    String getDiaryContent();
    boolean getIsUserLiked();
    Integer getLikeCount();
    String getStartDate();
    String getEndDate();
    ArrayList<TagEntity> getTags();
    ArrayList<CommentDto> getComments();
}
