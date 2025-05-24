// com.konnect.tag.TagService
package com.konnect.tag;

import com.konnect.tag.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final ChatClient chatClient;
    private final TagRepository tagRepository;

    public TagService(ChatClient.Builder chatClientBuilder, TagRepository tagRepository) {
        this.chatClient = chatClientBuilder.build();
        this.tagRepository = tagRepository;
    }

    private static final Pattern SPLIT = Pattern.compile("[,\\n]");
    private static final int TAG_LIMIT = 3;

    @Transactional(readOnly = true)
    public List<TagDto> generate(String content) {

        /* 1. 프롬프트 생성 */
        List<String> tagNames = tagRepository.findAll()
                .stream()
                .map(TagEntity::getName)
                .toList();
        String prompt = PromptBuilder.buildPrompt(tagNames, content);

        /* 2. GPT 호출 → 한 줄로 응답 문자열 */
        ChatClient.CallResponseSpec res = chatClient.prompt()
                .user(prompt)
                .call();

        // CallResponseSpec
        String answer = res.content();

        /* 3. 태그 이름 파싱 */
        Set<String> parsed = SPLIT.splitAsStream(answer)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .limit(TAG_LIMIT)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        /* 4. DB 매핑 (한글 우선, 부족하면 영문 컬럼) */
        List<TagEntity> tags = tagRepository.findByNameIn(List.copyOf(parsed));
        if (tags.size() < parsed.size()) {
            List<String> remain = parsed.stream()
                    .filter(n -> tags.stream().noneMatch(t -> t.getName().equals(n)))
                    .toList();
            tags.addAll(tagRepository.findByNameEngIn(remain));
        }

        /* 5. DTO 변환 */
        return tags.stream()
                .map(t -> new TagDto(t.getTagId(), t.getName(), t.getNameEng()))
                .toList();
    }
}