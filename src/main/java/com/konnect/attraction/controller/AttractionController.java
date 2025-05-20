package com.konnect.attraction.controller;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.service.AttractionService;
import com.konnect.util.CursorPage;
import com.konnect.util.SearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/all/attractions")
@Tag(name = "관광지", description = "관광지 관련 API")
public class AttractionController {

    private final AttractionService attractionService;

    @Operation(
            summary = "관광지 목록 조회",
            description = """
                        관광지 목록 조회(커서 기반 페이지네이션과 검색 조건 활용)
                        - 커서 기반 페이징: 마지막 contentId를 cursorId로 전달  
                        - 검색 조건: 제목(title), 콘텐츠 타입 ID(contentTypeId), 콘텐츠 타입 이름(contentTypeName)
                    """
    )
    @GetMapping
    public ResponseEntity<CursorPage<AttractionDTO>> getAttractions(
            @Parameter(description = "이전 페이지의 마지막 contentId (기본: 없음)")
            @RequestParam(required = false) Long cursorId,

            @Parameter(description = "페이지 크기 (기본값: 10)")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "제목 검색어 (부분 일치)")
            @RequestParam(required = false) String title,

            @Parameter(description = "콘텐츠 타입 ID (예: 12)")
            @RequestParam(required = false) String contentTypeId,

            @Parameter(description = "콘텐츠 타입 이름 (예: 관광지, 문화시설 등)")
            @RequestParam(required = false) String contentTypeName
    ) {
        SearchCondition condition = new SearchCondition();
        if (title != null) condition.put("title", title);
        if (contentTypeId != null) condition.put("contentTypeId", contentTypeId);
        if (contentTypeName != null) condition.put("contentTypeName", contentTypeName);

        CursorPage<AttractionDTO> result =
                attractionService.searchAttractions(cursorId, size, condition);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "관광지 상세 정보 조회",
            description = "관광지 ID를 이용하여 상세 정보를 조회합니다."
    )
    @GetMapping("/{attractionId}")
    public ResponseEntity<AttractionDTO> getAttractionDetail(
            @Parameter(description = "관광지 ID", example = "56644")
            @PathVariable("attractionId") Integer attractionId
    ) {
        AttractionDTO attraction = attractionService.getAttractionDetail(attractionId);
        System.out.println(attraction);
        return ResponseEntity.ok(attraction);
    }
}
