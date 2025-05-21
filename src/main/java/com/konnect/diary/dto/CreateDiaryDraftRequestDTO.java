package com.konnect.diary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
public class CreateDiaryDraftRequestDTO {
    private Optional<Long> diaryId = Optional.empty();

    @NotBlank
    private String title;

    private Long userId;

    private Optional<String> content = Optional.empty();

    private Optional<Long> areaId = Optional.empty();

    private List<Long> tags = new ArrayList<>();

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private Optional<String> startDate = Optional.empty();

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private Optional<String> endDate = Optional.empty();
}