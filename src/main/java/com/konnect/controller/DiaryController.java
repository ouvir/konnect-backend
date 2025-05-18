package com.konnect.controller;

import com.konnect.dto.CreateDiaryRequestDTO;
import com.konnect.dto.CreateDiaryResponseDTO;
import com.konnect.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<CreateDiaryResponseDTO> createDiary(
            @RequestPart("data") CreateDiaryRequestDTO requestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        CreateDiaryResponseDTO response = diaryService.createDiary(requestDTO, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
