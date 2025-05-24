package com.konnect.tag;

import java.util.List;

public class PromptBuilder {

    private static final String TEMPLATE = """
        당신은 여행 기록을 분석해 어울리는 태그 3개를 추천하는 역할입니다.
        태그는 반드시 아래 목록 중에서만 고르고, 쉼표(,)로 구분된 한글 이름만 출력하세요.

        태그 목록:
        %s

        여행 설명:
        %s
        """;

    public static String buildPrompt(List<String> tagNames, String content) {
        return TEMPLATE.formatted(String.join(", ", tagNames), content);
    }
}
