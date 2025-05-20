package com.konnect.diary.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.dto.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
import com.konnect.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/user/diaries/draft")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> createDiaryDraft(
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        requestDTO.setUserId(userDetails.getId());
        CreateDiaryResponseDTO response = diaryService.createDiaryDraft(requestDTO, thumbnail, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}