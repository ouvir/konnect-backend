package com.konnect.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
    private Long tagId;
    private String name;
    private String name_eng;

    public static TagResponseDTO from(TagEntity tag) {
        return new TagResponseDTO(tag.getTagId(), tag.getName(), tag.getNameEng());
    }
}