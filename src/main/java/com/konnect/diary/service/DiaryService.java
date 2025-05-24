package com.konnect.diary.service;

import com.konnect.diary.dto.*;
import com.konnect.diary.dto.request.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.response.CreateDiaryResponseDTO;
import com.konnect.diary.dto.response.DetailDiaryResponseDTO;
import com.konnect.diary.dto.response.ListDiaryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiaryService {
    CreateDiaryResponseDTO createDiary(
            CreateDiaryDraftRequestDTO requestDTO,
            Long userId,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    );

    CreateDiaryResponseDTO editDiary(
            CreateDiaryDraftRequestDTO dto,
            Long userId,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    );

    List<ListDiaryResponseDTO> fetchDiaries(Long areaId, boolean topOnly, DiarySortType sortedBy);

    List<ListDiaryResponseDTO> fetchMyDiaries(Long userId);

    DetailDiaryResponseDTO fetchDiaryDetail(Long diaryId, Long userId);
}