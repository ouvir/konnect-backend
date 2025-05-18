package com.konnect.service;

import com.konnect.dto.CreateDiaryRequestDTO;
import com.konnect.dto.CreateDiaryResponseDTO;
import com.konnect.dto.TagResponseDTO;
import com.konnect.entity.DiaryEntity;
import com.konnect.entity.DiaryTagEntity;
import com.konnect.entity.TagEntity;
import com.konnect.repository.DiaryRepository;
import com.konnect.repository.DiaryTagRepository;
import com.konnect.repository.TagRepository;
import com.konnect.util.ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryTagRepository diaryTagRepository;
    private final TagRepository tagRepository;
    private final ImageManager imageManager;

    @Override
    @Transactional
    public CreateDiaryResponseDTO createDiary(CreateDiaryRequestDTO requestDTO, List<MultipartFile> imageFiles) {
        DiaryEntity diary = DiaryEntity.builder()
                .userId(requestDTO.getWriterId())
                .areaCode(requestDTO.getAreaCode())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .imageTotalCount(imageFiles.size())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .build();
        DiaryEntity savedDiary = diaryRepository.save(diary);

        List<TagResponseDTO> tags = new ArrayList<>();
        for (Long tagId : requestDTO.getTags()) {
            TagEntity tag = tagRepository.getReferenceById(tagId); // 또는 findById()

            DiaryTagEntity diaryTag = new DiaryTagEntity();
            diaryTag.setDiary(savedDiary);
            diaryTag.setTag(tag);
            tags.add(TagResponseDTO.from(diaryTag.getTag()));

            diaryTagRepository.save(diaryTag);  // diarytags 테이블에 한 줄씩 insert됨
        }

        imageManager.saveImages(savedDiary.getDiaryId(), imageFiles);

        CreateDiaryResponseDTO response = CreateDiaryResponseDTO.from(savedDiary);
        response.setTags(tags);
        return response;
    }
}
