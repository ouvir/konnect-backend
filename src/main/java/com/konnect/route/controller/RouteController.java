package com.konnect.route.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.route.dto.*;
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
@Tag(name = "여행 경로", description = "다이어리에 포함된 루트 CRUD API (명소 정보 내장)")
public class RouteController {

    private final RouteService routeService;

    /* -------------------- CREATE -------------------- */
    @PostMapping
    @Operation(
            summary = "루트 생성",
            description = "다이어리(diaryId)에 방문 루트를 새로 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = RouteDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 diaryId", content = @Content)
    })
    public ResponseEntity<RouteDetailResponse> create(
            @Valid @RequestBody
            @Parameter(description = "루트 생성 요청 본문", required = true)
            RouteCreateRequest req,

            @AuthenticationPrincipal
            @Parameter(hidden = true) CustomUserPrincipal userDetails
    ) {
        RouteDetailResponse dto = routeService.create(req, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /* -------------------- LIST -------------------- */
    @GetMapping
    @Operation(
            summary = "루트 목록 조회",
            description = "특정 다이어리의 루트를 방문 일자·시각 오름차순으로 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RouteDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 diaryId", content = @Content)
    })
    public ResponseEntity<List<RouteDetailResponse>> list(
            @RequestParam
            @Parameter(description = "다이어리 PK", required = true, example = "3")
            Long diaryId
    ) {
        SearchCondition cond = new SearchCondition();
        cond.put("diaryId", diaryId.toString());
        return ResponseEntity.ok(routeService.list(cond));
    }

    /* -------------------- UPDATE -------------------- */
    @PutMapping("/{id}")
    @Operation(
            summary = "루트 수정",
            description = "부분 수정이 가능합니다. 필요한 필드만 전송하세요."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 id", content = @Content)
    })
    public ResponseEntity<Void> update(
            @PathVariable
            @Parameter(description = "루트 PK", required = true, example = "10")
            Long id,

            @Valid @RequestBody
            @Parameter(description = "루트 수정 요청 본문", required = true)
            RouteUpdateRequest req
    ) {
        routeService.update(id, req);
        return ResponseEntity.ok().build();
    }

    /* -------------------- DELETE -------------------- */
    @DeleteMapping("/{id}")
    @Operation(summary = "루트 삭제", description = "PK(id)로 루트를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 id", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "루트 PK", required = true, example = "10")
            Long id
    ) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* -------------------- REORDER -------------------- */
    @PostMapping("/reorder")
    @Operation(
            summary = "루트 순서 재정렬",
            description = """
                • 프론트에서 재정렬된 <code>routeIds</code>,
                  각 루트의 <code>visitedDates</code>, <code>visitedTimes</code>,
                  구간 <code>distances</code> 를 전송합니다.  
                • <code>distances</code> 길이는 routeIds - 1 이어야 하며,
                  마지막 루트의 distance 는 <code>null</code> 로 처리됩니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재정렬 성공"),
            @ApiResponse(responseCode = "400", description = "리스트 길이 불일치", content = @Content)
    })
    public ResponseEntity<Void> reorder(
            @Valid @RequestBody
            @Parameter(description = "루트 재정렬 요청", required = true)
            RouteReorderRequest req
    ) {
        List<Long>    ids   = req.routeIds();
        List<String>  dates = req.visitedDates();
        List<String>  times = req.visitedTimes();
        List<Double>  ds    = req.distances();

        if (ids.isEmpty()
                || ids.size() != dates.size()
                || ids.size() != times.size()
                || ds.size()  != ids.size() - 1) {
            throw new IllegalArgumentException("리스트 길이가 맞지 않습니다.");
        }

        for (int i = 0; i < ids.size(); i++) {
            Double distance = (i < ds.size()) ? ds.get(i) : null;
            routeService.update(
                    ids.get(i),
                    new RouteUpdateRequest(
                            dates.get(i),
                            times.get(i),
                            null, null, null,
                            distance
                    )
            );
        }
        return ResponseEntity.ok().build();
    }
}
