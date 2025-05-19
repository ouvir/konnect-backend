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

    private Long areaId;

    private List<Long> tags;

    private Long writerId;

    private LocalDate startDate;

    private LocalDate endDate;
}
