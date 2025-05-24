package com.konnect.diary.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateDiaryRouteDTO {
    private String date;
    private List<DiaryRouteDTO> items = new ArrayList<>();
}