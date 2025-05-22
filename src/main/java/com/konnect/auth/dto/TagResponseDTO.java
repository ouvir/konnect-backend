package com.konnect.auth.dto;

import com.konnect.entity.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDTO {
    private Long tagId;
    private String name;

    public static TagResponseDTO from(TagEntity tag) {
        return new TagResponseDTO(tag.getTagId(), tag.getName());
    }
}
