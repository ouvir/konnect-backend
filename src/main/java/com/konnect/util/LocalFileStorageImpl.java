package com.konnect.util;

import com.konnect.diary.service.exception.FileStorageRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

@Component
public class LocalFileStorageImpl implements FileStorage {

    @Value("${storage.image.path}")
    private String storagePath;

    @Override
    public void save(Long diaryId, String fileName, MultipartFile file) {
        try {
            Path directory = Paths.get(storagePath, diaryId.toString());
            Files.createDirectories(directory);
            Path target = directory.resolve(fileName);
            file.transferTo(target);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    @Override
    public void deleteDirectoryIfExists(Long diaryId) {
        Path dir = Paths.get(storagePath, diaryId.toString());
        if (!Files.exists(dir)) return;
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); }
                        catch (IOException e) { throw new FileStorageRuntimeException("삭제 실패: " + p, e); }
                    });
        } catch (IOException e) {
            throw new FileStorageRuntimeException("디렉토리 삭제 실패: diary " + diaryId, e);
        }
    }
}
