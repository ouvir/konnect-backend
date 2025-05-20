package com.konnect.util;

import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.diary.service.exception.FileStorageRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LocalFileStorageImpl implements FileStorage {

    @Value("${storage.image.path}")
    private String storagePath;

    @Override
    public void saveAll(
            Long diaryId,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        try {
            Path diaryDir = Paths.get(storagePath).resolve(diaryId.toString());
            Files.createDirectories(diaryDir);

            try (Stream<Path> entries = Files.list(diaryDir)) {
                List<Path> thumbs = entries
                        .filter(p -> p.getFileName().toString().startsWith("thumbnail"))
                        .collect(Collectors.toList());
                for (Path oldThumb : thumbs) {
                    Files.deleteIfExists(oldThumb);
                }
            }
            if (thumbnail != null && !thumbnail.isEmpty()) {
                String ext = getExtension(thumbnail.getOriginalFilename());
                Path thumbPath = diaryDir.resolve("thumbnail" + ext);
                thumbnail.transferTo(thumbPath.toFile());
            }

            Path imagesDir = diaryDir.resolve("images");
            if (imageFiles == null) {
                if (Files.exists(imagesDir)) {
                    Files.walk(imagesDir)
                            .sorted(Comparator.reverseOrder())
                            .forEach(p -> p.toFile().delete());
                }
            } else {
                // imageFiles non-null → 기존 폴더 비우고 새로 저장
                if (Files.exists(imagesDir)) {
                    Files.walk(imagesDir)
                            .sorted(Comparator.reverseOrder())
                            .forEach(p -> p.toFile().delete());
                }
                Files.createDirectories(imagesDir);

                for (int i = 0; i < Math.min(imageFiles.size(), 9); i++) {
                    MultipartFile mf = imageFiles.get(i);
                    if (mf.isEmpty()) continue;

                    String ext = getExtension(mf.getOriginalFilename());
                    String filename = (i + 1) + ext;  // 1.jpg, 2.png ...
                    Path imgPath = imagesDir.resolve(filename);
                    mf.transferTo(imgPath.toFile());
                }
            }

        } catch (IOException e) {
            System.out.println("LocalFileStorageImpl: " + e.getMessage());
            throw new DiaryRuntimeException("Failed to store files for diary " + diaryId);
        }
    }

    private String getExtension(String original) {
        if (original == null) return "";
        int idx = original.lastIndexOf('.');
        return (idx == -1) ? "" : original.substring(idx);
    }
}
