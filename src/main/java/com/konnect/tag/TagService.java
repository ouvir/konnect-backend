package com.konnect.tag;

import com.konnect.tag.dto.TagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagService {
    private final ChatClient chatClient;
    private final TagRepository tagRepository;

    private static final Set<String> STOP_TAGS = Set.of("여행");

    public TagService(ChatClient.Builder chatClientBuilder, TagRepository tagRepository) {
        this.chatClient = chatClientBuilder.build();
        this.tagRepository = tagRepository;
    }

    private static final Pattern SPLIT = Pattern.compile("[,\\n]");
    private static final int TAG_LIMIT = 3;

    @Transactional(readOnly = true)
    public List<TagDto> generate(String content) {
        return getRecommendedTags(content);
//        /* 1. 프롬프트 생성 */
//        List<String> tagNames = tagRepository.findAll()
//                .stream()
//                .map(TagEntity::getName)
//                .toList();
//        String prompt = PromptBuilder.buildPrompt(tagNames, content);
//        log.info("success prompt");
//        try {
//            ChatClient.CallResponseSpec res = CompletableFuture.supplyAsync(() -> chatClient
//                            .prompt()
//                            .user(prompt)
//                            .call())
//                    .orTimeout(15, TimeUnit.SECONDS)
//                    .join();
//
//            // CallResponseSpec
//            String answer = res.content();
//
//            log.info("success end prompt");
//
//            // Access the usage information
//            Usage usage = res.chatResponse().getMetadata().getUsage();
//            /* 3. 태그 이름 파싱 */
//            Set<String> parsed = SPLIT.splitAsStream(answer)
//                    .map(String::trim)
//                    .filter(s -> !s.isBlank())
//                    .limit(TAG_LIMIT)
//                    .collect(Collectors.toCollection(LinkedHashSet::new));
//
//            /* 4. DB 매핑 (한글 우선, 부족하면 영문 컬럼) */
//            List<TagEntity> tags = tagRepository.findByNameIn(List.copyOf(parsed));
//            if (tags.size() < parsed.size()) {
//                List<String> remain = parsed.stream()
//                        .filter(n -> tags.stream().noneMatch(t -> t.getName().equals(n)))
//                        .toList();
//                tags.addAll(tagRepository.findByNameEngIn(remain));
//            }
//
//            /* 5. DTO 변환 */
//            return tags.stream()
//                    .map(t -> new TagDto(
//                            t.getTagId(), t.getName(), t.getNameEng(),
//                            usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens()))
//                    .toList();
//        }
//        catch (CompletionException e) {
//            if (e.getCause() instanceof TimeoutException) {
//                log.error("GPT 요청이 15초 초과로 타임아웃되었습니다.");
//            } else {
//                log.error("GPT 호출 중 예외 발생: {}", e.getMessage(), e);
//            }
//            return getRandomTags();
//        } catch (Exception e) {
//            log.error("OpenAI 호출 실패 > 랜덤 생성 반환: {}", e.getMessage(), e);
//            return getRandomTags();
//        }
    }

    private List<TagDto> getRandomTags() {
        List<TagEntity> allTags = tagRepository.findAll();
        Collections.shuffle(allTags);

        return allTags.stream()
                .limit(3)
                .map(tag -> new TagDto(tag.getTagId(), tag.getName(), tag.getNameEng(),
                        null, null, null))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TagDto> getRecommendedTags(String content) {
        Set<String> userKeywords = extractKeywords(content);

        List<TagEntity> allTags = tagRepository.findAll();

        return allTags.stream()
                .map(tag -> {
                    Set<String> tagKor = extractKeywords(tag.getName());
                    Set<String> tagEng = extractKeywords(tag.getNameEng());
                    double simKor = jaccardSimilarity(userKeywords, tagKor);
                    double simEng = jaccardSimilarity(userKeywords, tagEng);
                    double similarity = Math.max(simKor, simEng);
                    return Map.entry(tag, similarity);
                })
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(TAG_LIMIT)
                .map(entry -> {
                    TagEntity tag = entry.getKey();
                    return new TagDto(tag.getTagId(), tag.getName(), tag.getNameEng(),
                            null, null, null);
                })
                .collect(Collectors.toList());
    }

    private Set<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) return Set.of();
        return Arrays.stream(text
                        .replaceAll("[^가-힣a-zA-Z0-9\\s]", "") // 특수문자 제거
                        .toLowerCase()
                        .split("\\s+"))
                .map(String::trim)
                .filter(word -> word.length() > 1)
                .filter(word -> !STOP_TAGS.contains(word))
                .collect(Collectors.toSet());
    }

    private double jaccardSimilarity(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return (double) intersection.size() / union.size();
    }
}