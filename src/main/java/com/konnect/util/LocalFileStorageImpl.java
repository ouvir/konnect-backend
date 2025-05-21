package com.konnect.util;

import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.diary.service.exception.FileStorageRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
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

    public String loadThumbnailBase64(Long diaryId) {
        Path diaryDir = Paths.get(storagePath, diaryId.toString());
        if (!Files.exists(diaryDir) || !Files.isDirectory(diaryDir)) {
            return null;
        }

        // thumbnail.* 패턴으로 디렉터리 스캔
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(diaryDir, "thumbnail.*")) {

            for (Path thumbPath : stream) {
                if (Files.isRegularFile(thumbPath)) {
                    byte[] data = Files.readAllBytes(thumbPath);
                    // 확장자 기반으로 MIME 타입 추출
                    String mimeType = Files.probeContentType(thumbPath);
                    if (mimeType == null) {
                        // probe 실패 시, 확장자로 유추
                        String ext = getExtension(thumbPath.getFileName().toString()).toLowerCase();
                        switch (ext) {
                            case ".png":  mimeType = "image/png";  break;
                            case ".gif":  mimeType = "image/gif";  break;
                            case ".bmp":  mimeType = "image/bmp";  break;
                            case ".webp": mimeType = "image/webp"; break;
                            default:      mimeType = "image/jpeg";
                        }
                    }
                    return "data:" + mimeType + ";base64," +
                            Base64.getEncoder().encodeToString(data);
                }
            }
            // thumbnail.* 파일이 하나도 없으면 null
            return null;

        } catch (IOException e) {
            throw new DiaryRuntimeException("Failed to load thumbnail for diary " + diaryId);
        }
    }

    @Override
    public ImageData loadImage(Long diaryId) {
        try {
            Path diaryDir = Paths.get(storagePath, diaryId.toString());

            // 1) 썸네일 로드
            String thumbnailBase64 = null;
            Path thumbPath = diaryDir.resolve("thumbnail.jpg");
            if (Files.exists(thumbPath)) {
                byte[] thumbBytes = Files.readAllBytes(thumbPath);
                String mime = "image/" + getExtension(thumbPath.getFileName().toString()).substring(1);
                thumbnailBase64 = "data:" + mime + ";base64," +
                        Base64.getEncoder().encodeToString(thumbBytes);
            }

            List<String> imagesBase64 = new ArrayList<>();
            Path imagesDir = diaryDir.resolve("images");
            if (Files.exists(imagesDir) && Files.isDirectory(imagesDir)) {
                try (Stream<Path> paths = Files.list(imagesDir).sorted()) {
                    paths.forEach(path -> {
                        try {
                            byte[] imgBytes = Files.readAllBytes(path);
                            String mime = "image/" + getExtension(path.getFileName().toString()).substring(1);
                            imagesBase64.add("data:" + mime + ";base64," +
                                    Base64.getEncoder().encodeToString(imgBytes));
                        } catch (IOException ignored) {
                        }
                    });
                }
            }

            return new ImageData(thumbnailBase64, imagesBase64);
        } catch (IOException e) {
            throw new DiaryRuntimeException("Failed to load images for diary " + diaryId);
        }
    }

    private String getExtension(String original) {
        if (original == null) return "";
        int idx = original.lastIndexOf('.');
        return (idx == -1) ? "" : original.substring(idx);
    }
}
