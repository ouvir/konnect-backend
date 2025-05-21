package com.konnect.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorage {
    public void saveAll(Long diaryId, MultipartFile thumbnail, List<MultipartFile> imageFiles);
}