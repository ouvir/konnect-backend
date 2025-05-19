package com.konnect.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateDiaryRequestDTO {
    private String title;

    private String content;

    private Long areaId;

    private List<Long> tags;

    private Long writerId;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 yyyy-MM-dd 이어야 합니다.")
    private String endDate;
}
