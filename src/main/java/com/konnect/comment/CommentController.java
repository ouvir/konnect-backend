package com.konnect.comment;

import com.konnect.comment.dto.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "댓글", description = "댓글 및 대댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest request) {
        commentService.createComment(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "대댓글 작성", description = "부모 댓글에 대한 대댓글을 작성합니다.")
    @PostMapping("/reply")
    public ResponseEntity<?> createReply(@RequestBody CommentRequest request) {
        commentService.createReply(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 수정", description = "댓글이나 대댓글의 내용을 수정합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long id,
            @RequestBody @Schema(description = "수정할 댓글 내용", example = "수정된 내용입니다") String content
    ) {
        commentService.updateComment(id, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글 또는 대댓글을 논리적으로 삭제합니다. 실제 DB에서 제거하지 않습니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}