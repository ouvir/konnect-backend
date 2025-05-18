package com.konnect.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageManager {

    private final FileStorage fileStorage;

    public void saveImages(Long postId, List<MultipartFile> imageFiles) {
        if (imageFiles == null || imageFiles.isEmpty()) return;

        fileStorage.deleteDirectoryIfExists(postId);

        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile image = imageFiles.get(i);
            String filename = (i + 1) + getExtension(image.getOriginalFilename());
            fileStorage.save(postId, filename, image);
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}