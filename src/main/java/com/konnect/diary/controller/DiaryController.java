package com.konnect.diary.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.dto.*;
import com.konnect.diary.dto.request.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.response.CreateDiaryResponseDTO;
import com.konnect.diary.dto.response.DetailDiaryResponseDTO;
import com.konnect.diary.dto.response.ListDiaryResponseDTO;
import com.konnect.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DiaryController implements DiaryAPI {
  
    private final DiaryService diaryService;

    @PostMapping(path = "/user/diaries")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> createDiary(
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        CreateDiaryResponseDTO dto =
                diaryService.createDiary(requestDTO, userDetails == null ? 1 : userDetails.getId(), thumbnail, imageFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Override
    @PostMapping("/user/diaries/{diaryId}")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> editDiary(
            @PathVariable Long diaryId,
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        requestDTO.setDiaryId(Optional.ofNullable(diaryId));
        CreateDiaryResponseDTO dto =
                diaryService.editDiary(requestDTO, userDetails.getId(), thumbnail, imageFiles);
        HttpStatus status = dto.getDiaryId() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(dto);
    }

    @GetMapping("/all/diaries")
    public ResponseEntity<List<ListDiaryResponseDTO>> fetchDiaries(
            @RequestParam(name = "areaId") Long areaId,
            @RequestParam(name = "topOnly", defaultValue = "true") boolean topOnly,
            @RequestParam(name = "sortedBy", defaultValue = "MOST_LIKED") DiarySortType sortedBy
    ) {
        List<ListDiaryResponseDTO> response = diaryService.fetchDiaries(areaId, topOnly, sortedBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/diaries")
    public ResponseEntity<List<ListDiaryResponseDTO>> fetchMyDiaries(
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        List<ListDiaryResponseDTO> response = diaryService.fetchMyDiaries(userDetails == null ? 1 : userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/diaries/{diaryId}")
    public ResponseEntity<DetailDiaryResponseDTO> fetchDiaryById(
            @PathVariable Long diaryId,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        DetailDiaryResponseDTO dto = diaryService.fetchDiaryDetail(diaryId, userDetails == null ? 1 : userDetails.getId());
        return ResponseEntity.ok(dto);
    }
}