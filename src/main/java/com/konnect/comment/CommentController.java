package com.konnect.comment;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.comment.dto.CommentDto;
import com.konnect.comment.dto.CreateCommentRequest;
import com.konnect.comment.dto.CreateReplyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/comments")
@Tag(name = "댓글", description = "댓글 및 대댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새 댓글을 작성합니다.",
            responses = @ApiResponse(responseCode = "200", description = "작성된 댓글", content = @Content(schema = @Schema(implementation = CommentDto.class))))
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@AuthenticationPrincipal CustomUserPrincipal userDetails,
                                                    @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(userDetails.getId(), request));
    }

    @Operation(summary = "대댓글 작성", description = "부모 댓글에 대한 대댓글을 작성합니다.",
            responses = @ApiResponse(responseCode = "200", description = "작성된 대댓글", content = @Content(schema = @Schema(implementation = CommentDto.class))))
    @PostMapping("/reply")
    public ResponseEntity<CommentDto> createReply(@AuthenticationPrincipal CustomUserPrincipal userDetails,
                                                  @RequestBody CreateReplyRequest request) {
        return ResponseEntity.ok(commentService.createReply(userDetails.getId(), request));
    }

    @Operation(summary = "댓글 수정", description = "댓글이나 대댓글의 내용을 수정합니다.",
            responses = @ApiResponse(responseCode = "200", description = "수정된 댓글", content = @Content(schema = @Schema(implementation = CommentDto.class))))
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@AuthenticationPrincipal CustomUserPrincipal userDetails,
                                                    @PathVariable("id") Long id,
                                                    @RequestBody String content) {
        return ResponseEntity.ok(commentService.updateComment(userDetails.getId(), id, content));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 또는 대댓글을 논리적으로 삭제합니다. 실제 DB에서 제거하지 않습니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CustomUserPrincipal userDetails,
                                           @PathVariable("id") Long id) {
        commentService.deleteComment(userDetails.getId(), id);
        return ResponseEntity.ok().build();
    }
}
