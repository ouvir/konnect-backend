package com.konnect.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorage {
    void saveAll(Long diaryId, MultipartFile thumbnail, List<MultipartFile> imageFiles);
    String loadThumbnailBase64(Long diaryId);
    ImageData loadImage(Long diaryId);
}