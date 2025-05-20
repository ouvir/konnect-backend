package com.konnect.diary.service;

import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.entity.DiaryTagEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.diary.repository.DiaryTagRepository;
import com.konnect.diary.dto.CreateDiaryRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
import com.konnect.dto.ListDiaryResponseDTO;
import com.konnect.repository.*;
import com.konnect.user.repository.UserRepository;
import com.konnect.util.ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryTagRepository diaryTagRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;

    private final ImageManager imageManager;

    @Override
    @Transactional
    public CreateDiaryResponseDTO createDiary(CreateDiaryRequestDTO requestDTO, List<MultipartFile> imageFiles) {
        if (!validCreateRequestByStatus(requestDTO)) {
            throw new IllegalArgumentException("status가 COMPLETE인 경우 필수 필드가 누락되었습니다.");
        }

        DiaryEntity diary = DiaryEntity.builder()
                .title(requestDTO.getTitle())
                .user(userRepository.getReferenceById(requestDTO.getUserId()))
                .status(requestDTO.getStatus())
                .content(requestDTO.getContent().orElseThrow())
                .area(areaRepository.getReferenceById(requestDTO.getAreaId().orElseThrow()))
                .startDate(requestDTO.getStartDate().orElseThrow())
                .endDate(requestDTO.getEndDate().orElseThrow())
                .build();
        DiaryEntity savedDiary = diaryRepository.save(diary);

        List<DiaryTagEntity> tags = new ArrayList<>();
        for (Long tagId: requestDTO.getTags()) {
            DiaryTagEntity diaryTag = new DiaryTagEntity();
            diaryTag.setDiary(diary);
            diaryTag.setTag(tagRepository.getReferenceById(tagId));
            tags.add(diaryTagRepository.save(diaryTag));
        }
        diary.setTags(tags);

        imageManager.saveImages(savedDiary.getDiaryId(), imageFiles);
        List<String> base64Images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            try {
                byte[] bytes = file.getBytes();
                String b64 = Base64.getEncoder().encodeToString(bytes);
                base64Images.add(b64);
            } catch (IOException e) {
                throw new UncheckedIOException("이미지 변환에 실패했습니다: " + file.getOriginalFilename(), e);
            }
        }

        return CreateDiaryResponseDTO.from(diary, base64Images);
    }

    private boolean validCreateRequestByStatus(CreateDiaryRequestDTO dto) {
        if (dto.getStatus().equals("editing")) {
            return true;
        }

        return dto.getContent().isPresent()
                && dto.getAreaId().isPresent()
                && dto.getStartDate().isPresent()
                && dto.getEndDate().isPresent()
                && dto.getTags().size() == 3;
    }

    @Override
    public List<ListDiaryResponseDTO> fetchDiaryList() {

        return null;
    }
}
