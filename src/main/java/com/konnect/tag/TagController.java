package com.konnect.tag;

import com.konnect.tag.dto.GenerateTagRequestDTO;
import com.konnect.tag.dto.TagDto;
import com.konnect.tag.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/ai-tags")
@Tag(name = "AI 태그", description = "AI 기반 태그 추천 API")
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "AI 태그 추천",
            description = "제목/내용과 기존 태그를 기반으로 나머지 태그를 추천합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "추천된 태그 리스트",
                            content = @Content(schema = @Schema(implementation = TagDto.class)))
            }
    )
    @PostMapping("/generate")
    public ResponseEntity<List<TagDto>> generateTags(@RequestBody GenerateTagRequestDTO request) {
        return ResponseEntity.ok(tagService.generateTags(request));
    }
}