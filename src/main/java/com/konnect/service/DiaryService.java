package com.konnect.service;

import com.konnect.dto.CreateDiaryRequestDTO;

public interface DiaryService {
    void createDiary(CreateDiaryRequestDTO requestDTO);
}