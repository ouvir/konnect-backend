package com.konnect.diary.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.dto.*;
import com.konnect.diary.dto.request.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.request.EditDiaryRequestDTO;
import com.konnect.diary.dto.response.CreateDiaryResponseDTO;
import com.konnect.diary.dto.response.DetailDiaryResponseDTO;
import com.konnect.diary.dto.response.ListDiaryResponseDTO;
import com.konnect.diary.service.DiaryService;
import com.konnect.util.Base64DecodedMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Slf4j
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
        CreateDiaryResponseDTO dto = diaryService.createDiary(
                        requestDTO,
                        userDetails == null ? 1 : userDetails.getId(),
                        thumbnail,
                        imageFiles
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/user/diaries/{diaryId}")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> editDiary(
            @PathVariable Long diaryId,
            @RequestPart("data") EditDiaryRequestDTO req,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "images",    required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserPrincipal user
    ) {
        /* 1. diaryId 주입 */
        req.setDiaryId(Optional.ofNullable(diaryId));

        /* 2. 썸네일 변환 */
        if (thumbnail == null && req.hasThumbnail()) {
            // Base64 문자열에서 content-type 추출
            String dataUri = req.getThumbnail();
            String contentType = dataUri.substring(dataUri.indexOf(':') + 1, dataUri.indexOf(';')); // ex) image/jpeg
            String ext = contentType.substring(contentType.indexOf('/') + 1);                       // ex) jpeg

            thumbnail = new Base64DecodedMultipartFile(
                    dataUri,
                    "thumbnail",
                    "thumbnail." + ext        // jpeg, png 등 실제 확장자
            );
        }

        /* 3. 본문 이미지 변환 */
        if (req.hasImage()) {
            if (imageFiles == null) imageFiles = new ArrayList<>();
            int idx = imageFiles.size() + 1;
            for (String b64 : req.getImages()) {
                imageFiles.add(
                        new Base64DecodedMultipartFile(b64, "image" + idx, "image" + idx++)
                );
            }
        }

        /* 4. 서비스 호출 */
        long memberId = (user == null) ? 1L : user.getId();
        CreateDiaryResponseDTO dto =
                diaryService.editDiary(req, memberId, thumbnail, imageFiles);

        HttpStatus status = (dto.getDiaryId() == null) ? HttpStatus.CREATED : HttpStatus.OK;
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
        List<ListDiaryResponseDTO> response = diaryService
                .fetchMyDiaries(userDetails == null ? 1 : userDetails.getId());
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

    @DeleteMapping("/user/diaries/{diaryId}")
    public ResponseEntity<HttpStatus> deleteDiary(
            @PathVariable Long diaryId,
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    ) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
}