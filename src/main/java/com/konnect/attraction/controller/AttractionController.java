package com.konnect.attraction.controller;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.service.AttractionService;
import com.konnect.util.CursorPage;
import com.konnect.util.OffsetPage;
import com.konnect.util.SearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/all/attractions")
@Tag(name = "관광지", description = "관광지 관련 API")
public class AttractionController {

    private final AttractionService attractionService;

    @Operation(
            summary = "관광지 목록 조회",
            description = """
            관광지 목록을 페이지 단위로 조회합니다 (Offset 기반 페이징).
            
            - `page`: 0부터 시작하는 페이지 번호 (기본값: 0)
            - `size`: 한 페이지에 포함할 데이터 개수 (기본값: 10)
            - `title`: 관광지 제목 키워드 (부분 일치 검색)
            - `contentTypeId`: 콘텐츠 유형 ID (예: 12는 관광지)
            - `contentTypeName`: 콘텐츠 유형 이름 (예: 관광지, 문화시설 등)
        """
    )
    @GetMapping
    public ResponseEntity<OffsetPage<AttractionDTO>> getAttractions(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "한 페이지 당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "관광지 제목 키워드", example = "서울")
            @RequestParam(required = false) String title,

            @Parameter(description = "콘텐츠 타입 ID", example = "")
            @RequestParam(required = false) String contentTypeId,

            @Parameter(description = "콘텐츠 타입 이름", example = "관광지")
            @RequestParam(required = false) String contentTypeName
    ) {
        SearchCondition condition = new SearchCondition();
        condition.put("title", title);
        condition.put("contentTypeId", contentTypeId);
        condition.put("contentTypeName", contentTypeName);

        OffsetPage<AttractionDTO> result = attractionService.searchAttractions(page, size, condition);
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
