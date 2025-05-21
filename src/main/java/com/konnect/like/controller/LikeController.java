package com.konnect.like.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.like.service.LikeService;
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
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/diaries/{diaryId}/like")
    public ResponseEntity likeDiary(
            @PathVariable Long diaryId,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        try {
            likeService.likeDiary(diaryId, userDetails.getId());
            return ResponseEntity.ok().build();
        } catch (DiaryRuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
