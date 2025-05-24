package com.konnect.tag;

import com.konnect.tag.dto.GenerateTagRequestDTO;
import com.konnect.tag.dto.TagDto;
import com.konnect.tag.TagEntity;
import com.konnect.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDto> generateTags(GenerateTagRequestDTO request) {
        List<TagEntity> allTags = tagRepository.findAll();

        Set<Long> existingTagIds = new HashSet<>(request.getExistingTags());
        List<TagEntity> availableTags = allTags.stream()
                .filter(tag -> !existingTagIds.contains(tag.getTagId()))
                .collect(Collectors.toList());

        int numberToPick = 3 - existingTagIds.size();
        Collections.shuffle(availableTags);

        return availableTags.stream()
                .limit(numberToPick)
                .map(tag -> new TagDto(tag.getTagId(), tag.getName()))
                .collect(Collectors.toList());
    }
}