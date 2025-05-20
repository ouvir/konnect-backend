package com.konnect.diary.service;

import com.konnect.diary.dto.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.diary.repository.DiaryTagRepository;
import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.entity.TagEntity;
import com.konnect.repository.AreaRepository;
import com.konnect.repository.TagRepository;
import com.konnect.user.repository.UserRepository;
import com.konnect.util.ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryTagRepository diaryTagRepository;
    private final TagRepository tagRepository;
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;

    private final ImageManager imageManager;

    @Override
    @Transactional
    public CreateDiaryResponseDTO createDiaryDraft(
            CreateDiaryDraftRequestDTO requestDTO,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        DiaryEntity diary = requestDTO.getDiaryId()
                .map(id -> diaryRepository.findById(id)
                        .filter(d -> "editing".equals(d.getStatus()))
                        .orElseThrow(() -> new DiaryRuntimeException("Draft not found for id: " + id))
                )
                .orElseGet(DiaryEntity::new);

        try {
            diary.setTitle(requestDTO.getTitle());
            diary.setContent(requestDTO.getContent().orElse(null));
            diary.setArea(requestDTO.getAreaId()
                    .map(areaRepository::getReferenceById)
                    .orElse(null)
            );
            diary.setStartDate(requestDTO.getStartDate().orElse(null));
            diary.setEndDate(requestDTO.getEndDate().orElse(null));
            diary.setStatus("editing");
            Long userId = requestDTO.getUserId();
            diary.setUser(userRepository.getReferenceById(userId));

            diary = diaryRepository.save(diary);
            syncTags(diary, requestDTO.getTags());

            byte[] thumbBytes = null;
            if (thumbnail != null && !thumbnail.isEmpty()) {
                try {
                    thumbBytes = thumbnail.getBytes();
                } catch (IOException e) {
                    throw new DiaryRuntimeException("Failed to read thumbnail bytes");
                }
            }

            List<byte[]> imagesBytes = new ArrayList<>();
            if (imageFiles != null) {
                for (int i = 0; i < Math.min(imageFiles.size(), 9); i++) {
                    MultipartFile mf = imageFiles.get(i);
                    if (mf.isEmpty()) continue;
                    try {
                        imagesBytes.add(mf.getBytes());
                    } catch (IOException e) {
                        throw new DiaryRuntimeException("Failed to read image bytes");
                    }
                }
            }

            try {
                imageManager.saveAllImages(diary.getDiaryId(), thumbnail, imageFiles);
            } catch (Exception ex) {
                throw new DiaryRuntimeException(
                        "Failed to store images for diary " + diary.getDiaryId());
            }

            String thumbnailBase64 = null;
            if (thumbBytes != null) {
                String prefix = "data:" + thumbnail.getContentType() + ";base64,";
                thumbnailBase64 = prefix + Base64.getEncoder()
                        .encodeToString(thumbBytes);
            }
            List<String> imagesBase64 = imagesBytes.stream()
                    .map(bytes -> "data:image/*;base64," +
                            Base64.getEncoder().encodeToString(bytes))
                    .collect(Collectors.toList());

            return CreateDiaryResponseDTO.from(diary, thumbnailBase64, imagesBase64);
        } catch (Exception ex) {
            throw new DiaryRuntimeException(
                    "Failed to create or update diary draft: " + ex.getMessage()
            );
        }
    }

    private void syncTags(DiaryEntity diary, List<Long> tagIds) {
        try {
            diaryTagRepository.deleteByDiary(diary);
            diary.getTags().clear();

            tagIds.stream()
                    .limit(3)
                    .forEach(tagId -> {
                        TagEntity tag = tagRepository.getReferenceById(tagId);
                        DiaryTagEntity dt = new DiaryTagEntity();
                        dt.setDiary(diary);
                        dt.setTag(tag);
                        diaryTagRepository.save(dt);
                        diary.getTags().add(dt);
                    });
        } catch (Exception ex) {
            System.out.println("DiaryServiceImpl: syncTags" + ex.getMessage());
            throw new DiaryRuntimeException("Failed to sync tags: " + ex.getMessage());
        }
    }

}
