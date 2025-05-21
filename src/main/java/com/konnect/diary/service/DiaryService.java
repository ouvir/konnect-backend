package com.konnect.diary.service;

import com.konnect.diary.dto.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
//import com.konnect.dto.ListDiaryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiaryService {
    CreateDiaryResponseDTO createDiaryDraft(
            CreateDiaryDraftRequestDTO requestDTO,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    );
}