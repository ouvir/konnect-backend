package com.konnect.like.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "좋아요", description = "다이어리 좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "다이어리에 좋아요 추가", description = "지정된 다이어리에 좋아요를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 추가 성공"),
            @ApiResponse(responseCode = "404", description = "해당 다이어리를 찾을 수 없음")
    })
    @PostMapping("/diaries/{diaryId}/like")
    public ResponseEntity likeDiary(
            @PathVariable Long diaryId,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        try {
            likeService.addLike(diaryId, userDetails.getId());
            return ResponseEntity.ok().build();
        } catch (DiaryRuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "다이어리 좋아요 취소", description = "지정된 다이어리에 대한 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "해당 다이어리를 찾을 수 없음")
    })
    @PostMapping("/diaries/{diaryId}/unlike")
    public ResponseEntity unlikeDiary(
            @PathVariable Long diaryId,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        try {
            likeService.removeLike(diaryId, userDetails.getId());
            return ResponseEntity.ok().build();
        } catch (DiaryRuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
