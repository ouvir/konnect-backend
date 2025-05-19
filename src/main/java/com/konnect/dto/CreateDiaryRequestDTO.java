package com.konnect.dto;

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

    // TODO: 지역 코드는 변경될 예정
    private Long areaCode;

    private List<Long> tags;

    private Long writerId;

    private LocalDate startDate;

    private LocalDate endDate;
}
