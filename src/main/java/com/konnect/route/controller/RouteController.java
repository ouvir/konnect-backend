package com.konnect.route.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.route.dto.RouteCreateRequest;
import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.route.dto.RouteReorderRequest;
import com.konnect.route.dto.RouteUpdateRequest;
import com.konnect.route.service.RouteService;
import com.konnect.util.SearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/routes")
@RequiredArgsConstructor
@Tag(name = "여행 경로", description = "다이어리에 포함된 여행 루트 CRUD API")
public class RouteController {

    private final RouteService routeService;

    /* -------------------- CREATE -------------------- */
    @Operation(
            summary = "루트 생성",
            description = "자신의 다이어리(diaryId)에 새 방문 루트를 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = RouteDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 diaryId 또는 attractionNo", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RouteDetailResponse> create(
            @Valid @RequestBody @Parameter(description = "루트 생성 요청 본문", required = true)
            RouteCreateRequest req,
            @Parameter(hidden = true)
            @AuthenticationPrincipal
            CustomUserPrincipal userDetails
    ) {
        System.out.println(req);
        System.out.println(userDetails.getId());

        var dto = routeService.create(req, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /* -------------------- LIST -------------------- */
    @Operation(
            summary = "루트 목록 조회",
            description = "특정 다이어리의 루트를 방문 일자/시각 오름차순으로 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RouteDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 diaryId", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<RouteDetailResponse>> list(
            @Parameter(description = "다이어리 PK", required = true, example = "3")
            @RequestParam Long diaryId,

            @Parameter(description = "방문 일자(yyyy-MM-dd)", example = "2025-05-23")
            @RequestParam(required = false) String visitedDate
    ) {
        SearchCondition cond = new SearchCondition();
        cond.put("diaryId", String.valueOf(diaryId));
        if (visitedDate != null) cond.put("visitedDate", visitedDate);
        return ResponseEntity.ok(routeService.list(cond));
    }

    /* -------------------- UPDATE -------------------- */
    @Operation(summary = "루트 수정",
            description = "필요한 필드만 전송해 부분 수정이 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 id / attraction", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "루트 PK", required = true, example = "10")
            @PathVariable Long id,
            @RequestBody
            @Valid RouteUpdateRequest req
    ) {
        routeService.update(id, req);
        return ResponseEntity.ok().build();
    }

    /* -------------------- DELETE -------------------- */
    @Operation(summary = "루트 삭제", description = "PK(id)로 단일 루트를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 id", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "루트 PK", required = true, example = "10")
            @PathVariable Long id) {

        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reorder")
    @Operation(
            summary = "루트 순서 재정렬",
            description = """
                        • 프론트에서 정렬된 <code>routeIds</code>,
                          각 루트의 <code>visitedDates</code>, <code>visitedTimes</code>,
                          구간 <code>distances</code> 를 한 번에 전송합니다.  
                        • 마지막 루트는 distance 값이 없습니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재정렬 성공"),
            @ApiResponse(responseCode = "400", description = "리스트 길이 불일치", content = @Content)
    })
    public ResponseEntity<Void> reorder(
            @Valid @RequestBody RouteReorderRequest req) {

        List<Long> ids = req.routeIds();
        List<String> dates = req.visitedDates();
        List<String> times = req.visitedTimes();
        List<Double> ds = req.distances();

        /* ---------- 간단한 길이 검증 ---------- */
        if (ids.isEmpty()
                || ids.size() != dates.size()
                || ids.size() != times.size()
                || ds.size() != ids.size() - 1) {

            throw new IllegalArgumentException("리스트 길이가 맞지 않습니다.");
        }

        /* ---------- 순서대로 업데이트 ---------- */
        for (int i = 0; i < ids.size(); i++) {
            Double distance = (i < ds.size()) ? ds.get(i) : null;   // 마지막 루트는 null
            routeService.update(
                    ids.get(i),
                    new RouteUpdateRequest(
                            dates.get(i),
                            times.get(i),
                            null,           // attractionNo 변경 없음
                            distance
                    )
            );
        }

        return ResponseEntity.ok().build();
    }
}