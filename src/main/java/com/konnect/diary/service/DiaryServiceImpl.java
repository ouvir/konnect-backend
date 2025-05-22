package com.konnect.diary.service;

import com.konnect.auth.dto.TagResponseDTO;
import com.konnect.diary.dto.*;
import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.diary.repository.DiaryTagRepository;
import com.konnect.diary.repository.ListDiaryProjection;
import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.entity.TagEntity;
import com.konnect.repository.AreaRepository;
import com.konnect.repository.TagRepository;
import com.konnect.user.repository.UserRepository;
import com.konnect.util.FileStorage;
import com.konnect.util.ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final FileStorage fileStorage;

    @Override
    @Transactional
    public CreateDiaryResponseDTO createDiaryDraft(
            CreateDiaryDraftRequestDTO dto,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        return upsertAndSaveDraft(dto, thumbnail, imageFiles, false);
    }

    @Override
    @Transactional
    public CreateDiaryResponseDTO publishDraft(
            CreateDiaryDraftRequestDTO dto,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        return upsertAndSaveDraft(dto, thumbnail, imageFiles, true);
    }

    @Override
    public List<ListDiaryResponseDTO> fetchDiaries(Long areaId, boolean topOnly, DiarySortType sortedBy) {
        Pageable pageable = createPageable(topOnly, sortedBy);
        Page<ListDiaryProjection> pages = diaryRepository.findDiariesByArea(areaId, pageable);
        return toResponseList(pages);
    }

    @Override
    public List<ListDiaryResponseDTO> fetchMyDiaries(Long userId) {
        Pageable pageable = PageRequest.of(
                0, Integer.MAX_VALUE,
                Sort.by(Sort.Order.asc("status"), Sort.Order.desc("created_at"))
        );
        Page<ListDiaryProjection> pages = diaryRepository.fetchMyDiaries(userId, pageable);
        return toResponseList(pages);
    }

    // ———————————— 헬퍼 1: Pageable 생성 분리
    private Pageable createPageable(boolean topOnly, DiarySortType sortType) {
        Sort sort = switch (sortType) {
            case RECENT     -> Sort.by(Sort.Direction.DESC, "created_at");
            case MOST_LIKED -> Sort.by(Sort.Direction.DESC, "likeCount");
        };
        int size = topOnly ? 4 : Integer.MAX_VALUE;
        return PageRequest.of(0, size, sort);
    }

    // ———————————— 헬퍼 2: Projection → Response 로 전환
    private List<ListDiaryResponseDTO> toResponseList(Page<ListDiaryProjection> pages) {
        return pages.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ———————————— 헬퍼 3: 개별 Projection → DTO 변환
    private ListDiaryResponseDTO toResponse(ListDiaryProjection p) {
        List<TagResponseDTO> tags = diaryTagRepository
                .findTop3ByDiary_DiaryIdOrderByIdAsc(p.getDiaryId())
                .stream()
                .map(t -> new TagResponseDTO(t.getTagId(), t.getName()))
                .collect(Collectors.toList());

        String thumbnail = fileStorage.loadThumbnailBase64(p.getDiaryId());
        AreaDTO area     = new AreaDTO(p.getAreaId(), p.getAreaName());

        return ListDiaryResponseDTO.builder()
                .diaryId(p.getDiaryId())
                .title(p.getTitle())
                .status(p.getStatus())
                .thumbnailImage(thumbnail)
                .area(area)
                .likeCount(p.getLikeCount())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .tags(tags)
                .build();
    }

    private CreateDiaryResponseDTO upsertAndSaveDraft(
            CreateDiaryDraftRequestDTO dto,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles,
            boolean publish
    ) {
        if (publish && dto.getDiaryId() == null) {
            throw new DiaryRuntimeException("Cannot publish: draftId must be provided");
        }

        DiaryEntity diary;
        if (dto.getDiaryId().isPresent()) {
            Long id = dto.getDiaryId().get();
            DiaryEntity existing = diaryRepository.findById(id)
                    .orElseThrow(() -> new DiaryRuntimeException(
                            "Draft not found: " + id));

            if ("published".equals(existing.getStatus())) {
                throw new DiaryRuntimeException(
                        "Cannot modify a published diary: " + id);
            }

            if (!"editing".equals(existing.getStatus())) {
                throw new DiaryRuntimeException(
                        "Draft is not in an editable state: " + id);
            }

            diary = existing;
        } else {
            diary = new DiaryEntity();
        }

        diary.setUser(userRepository.getReferenceById(dto.getUserId()));
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent().orElse(null));
        diary.setArea(dto.getAreaId()
                .map(areaRepository::getReferenceById)
                .orElse(null));
        diary.setStartDate(dto.getStartDate().orElse(null));
        diary.setEndDate(dto.getEndDate().orElse(null));
        String status = publish ? "published" : "editing";
        diary.setStatus(status);
        diary = diaryRepository.save(diary);
        syncTags(diary, dto.getTags());

        byte[] thumbBytes = readBytesOrNull(thumbnail,    "thumbnail");
        List<byte[]> imgBytes = readBytesList(imageFiles, "image");

        try {
            imageManager.saveAllImages(diary.getDiaryId(), thumbnail, imageFiles);
        } catch (Exception ex) {
            throw new DiaryRuntimeException("Failed to store images for diary " + diary.getDiaryId());
        }

        if (publish) {
            validateForPublish(diary, thumbnail, imageFiles);
            diary.setStatus("published");
            diary.setCreatedAt(LocalDateTime.now());
            diary = diaryRepository.save(diary);
        }

        String thumbBase64 = toBase64(thumbnail, thumbBytes);
        List<String> imgsBase64 = imgBytes.stream()
                .map(b -> "data:image/*;base64," + Base64.getEncoder().encodeToString(b))
                .collect(Collectors.toList());

        return CreateDiaryResponseDTO.from(diary, thumbBase64, imgsBase64);
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

    private void validateForPublish(
            DiaryEntity diary,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        if (diary.getTitle() == null || diary.getTitle().isBlank()
                || diary.getArea() == null
                || diary.getContent() == null || diary.getContent().isBlank()
                || diary.getTags().isEmpty()
                || diary.getStartDate() == null || diary.getEndDate() == null
        ) {
            throw new DiaryRuntimeException("Cannot publish: missing required fields");
        }
    }

    private byte[] readBytesOrNull(MultipartFile file, String who) {
        if (file == null || file.isEmpty()) return null;
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new DiaryRuntimeException("Failed to read " + who + " bytes");
        }
    }

    private List<byte[]> readBytesList(List<MultipartFile> files, String who) {
        if (files == null) return List.of();
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < Math.min(files.size(), 9); i++) {
            MultipartFile mf = files.get(i);
            if (mf.isEmpty()) continue;
            try {
                list.add(mf.getBytes());
            } catch (IOException e) {
                throw new DiaryRuntimeException("Failed to read " + who + " bytes");
            }
        }
        return list;
    }

    private String toBase64(MultipartFile file, byte[] bytes) {
        if (bytes == null) return null;
        String prefix = "data:" + file.getContentType() + ";base64,";
        return prefix + Base64.getEncoder().encodeToString(bytes);
    }

}