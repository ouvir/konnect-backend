package com.konnect.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDiaryIdAndParentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(Long diaryId);
    List<Comment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(Long parentId);
}