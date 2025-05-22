package com.konnect.like.service;

public interface LikeService {
    void addLike(Long diaryId, Long userId);
    void removeLike(Long diaryId, Long userId);
}
