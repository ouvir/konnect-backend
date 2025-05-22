package com.konnect.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageManager {

    private final FileStorage fileStorage;

    public void saveAllImages(
            Long diaryId,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        fileStorage.saveAll(diaryId, thumbnail, imageFiles);
    }

    public String loadThumbnailImage(Long diaryId) {
        return fileStorage.loadThumbnailBase64(diaryId);
    }

    public ImageData loadImage(Long diaryId) {
        return fileStorage.loadImage(diaryId);
    }
}