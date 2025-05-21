package com.konnect.diary.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.dto.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
import com.konnect.diary.dto.DiarySortType;
import com.konnect.diary.dto.ListDiaryResponseDTO;
import com.konnect.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(
            summary = "임시저장 다이어리 생성/수정",
            description = "드래프트(draft) 상태인 다이어리를 생성하거나 업데이트합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "드래프트 수정 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateDiaryResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "201",
                    description = "드래프트 생성 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateDiaryResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(path = "/user/diaries/draft")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> saveDraft(
            @Parameter(
                    description = "드래프트 저장 요청 DTO",
                    required = true,
                    schema = @Schema(implementation = CreateDiaryDraftRequestDTO.class)
            )
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,

            @Parameter(
                    description = "썸네일 이미지 파일 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,

            @Parameter(
                    description = "본문 이미지 파일 목록, 최대 9장 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,

            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        requestDTO.setUserId(userDetails.getId());
        CreateDiaryResponseDTO dto =
                diaryService.createDiaryDraft(requestDTO, thumbnail, imageFiles);
        HttpStatus status = dto.getDiaryId() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(dto);
    }

    @PostMapping("/user/diaries/{diaryId}/publish")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> publishDiary(
            @PathVariable Long diaryId,

            @Parameter(
                    description = "다이어리 게시 요청 DTO",
                    required = true,
                    schema = @Schema(implementation = CreateDiaryDraftRequestDTO.class)
            )
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,

            @Parameter(
                    description = "썸네일 이미지 파일 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,

            @Parameter(
                    description = "본문 이미지 파일 목록, 최대 9장 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,

            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        requestDTO.setUserId(userDetails.getId());
        requestDTO.setDiaryId(Optional.ofNullable(diaryId));
        CreateDiaryResponseDTO dto =
                diaryService.publishDraft(requestDTO, thumbnail, imageFiles);
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

}