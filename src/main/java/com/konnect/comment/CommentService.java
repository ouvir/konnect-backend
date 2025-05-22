package com.konnect.comment;

import com.konnect.comment.dto.CommentDto;
import com.konnect.comment.dto.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentDto> getCommentsByDiary(Long diaryId) {
        List<Comment> roots = commentRepository.findByDiaryIdAndParentIsNullAndIsDeletedFalseOrderByCreatedAtAsc(diaryId);
        return roots.stream()
                .map(this::toDtoWithChildren)
                .collect(Collectors.toList());
    }

    private CommentDto toDtoWithChildren(Comment comment) {
        CommentDto dto = CommentDto.from(comment);
        List<Comment> children = commentRepository.findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(comment.getCommentId());
        dto.setChildren(children.stream().map(CommentDto::from).collect(Collectors.toList()));
        return dto;
    }

    public void createComment(Long userId, CommentRequest request) {
        Comment comment = new Comment();
        comment.setDiaryId(request.getDiaryId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setCreatedAt(request.getCreatedAt());
        comment.setDeleted(false);
        comment.setParent(null);
        commentRepository.save(comment);
    }

    public void createReply(Long userId, CommentRequest request) {
        Comment parent = commentRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("부모 댓글 없음"));

        if (parent.getParent() != null) {
            throw new IllegalStateException("2단계 이상의 대댓글은 허용되지 않습니다.");
        }

        Comment reply = new Comment();
        reply.setDiaryId(request.getDiaryId());
        reply.setUserId(userId);
        reply.setContent(request.getContent());
        reply.setCreatedAt(request.getCreatedAt());
        reply.setDeleted(false);
        reply.setParent(parent);
        commentRepository.save(reply);
    }

    public void updateComment(Long userId, Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("본인의 댓글만 수정할 수 있습니다.");
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    public void deleteComment(Long userId, Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("본인의 댓글만 삭제할 수 있습니다.");
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}
