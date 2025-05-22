package com.konnect.like.service;

import com.konnect.diary.entity.DiaryEntity;
import com.konnect.diary.repository.DiaryRepository;
import com.konnect.diary.service.exception.DiaryRuntimeException;
import com.konnect.like.entity.LikeEntity;
import com.konnect.like.repository.LikeRepository;
import com.konnect.user.entity.UserEntity;
import com.konnect.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final DiaryRepository diaryRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Override
    public void addLike(Long diaryId, Long userId) {
        DiaryEntity diary = diaryRepository
                .findById(diaryId)
                .orElseThrow(() -> new DiaryRuntimeException("Diary not found with id: " + diaryId));

        LikeEntity likeEntity = likeRepository.findByDiary_DiaryIdAndUser_UserId(diaryId, userId);

        if (likeEntity == null) {
            UserEntity user = userRepository.getReferenceById(userId);
            likeEntity = new LikeEntity();
            likeEntity.setDiary(diary);
            likeEntity.setUser(user);
        } else {
            likeEntity.setDeleted(false);
        }

        likeRepository.save(likeEntity);
    }

    public void removeLike(Long diaryId, Long userId) {
        DiaryEntity diary = diaryRepository
                .findById(diaryId)
                .orElseThrow(() -> new DiaryRuntimeException("Diary not found with id: " + diaryId));

        LikeEntity likeEntity = likeRepository.findByDiary_DiaryIdAndUser_UserId(diaryId, userId);

        likeEntity.setDeleted(true);
        likeRepository.save(likeEntity);
    }
}
