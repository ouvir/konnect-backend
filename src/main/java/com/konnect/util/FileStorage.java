package com.konnect.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    void save(Long diaryId, String fileName, MultipartFile file);
    public void deleteDirectoryIfExists(Long postId);
}