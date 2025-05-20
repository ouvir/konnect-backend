package com.konnect.diary.service;

import com.konnect.diary.dto.CreateDiaryRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
//import com.konnect.dto.ListDiaryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DiaryService {
    CreateDiaryResponseDTO createDiary(CreateDiaryRequestDTO requestDTO, List<MultipartFile> imageFiles);

//    List<ListDiaryResponseDTO> fetchDiaryList();
}