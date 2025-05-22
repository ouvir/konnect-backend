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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/routes")
@RequiredArgsConstructor
@Tag(name = "여행 경로", description = "다이어리에 포함된 여행 루트 CRUD API")
public class RouteController {

    private final RouteService routeService;

    @Operation(
            summary = "루트 생성",
            description = """
                • 자신의 다이어리(diaryId)에 새 방문 루트를 등록합니다.
                • <code>orderIdx</code> 를 생략하면 서버가 자동으로 다음 순번을 부여합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 diaryId 또는 attractionNo",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<RouteDetailResponse> create(
            @Valid @RequestBody @Parameter(description = "루트 생성 요청 본문", required = true)
            RouteCreateRequest req,
            @AuthenticationPrincipal
            CustomUserPrincipal userDetails
    ) {
        RouteDetailResponse dto = routeService.create(req, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @Operation(
            summary = "루트 목록 조회",
            description = "특정 다이어리의 루트들을 순서(idx) 오름차순으로 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RouteDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 diaryId",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<RouteDetailResponse>> list(
            @Parameter(description = "다이어리 PK", required = true, example = "3")
            @RequestParam Long diaryId,

            @Parameter(description = "여행 날짜 인덱스", example = "1")
            @RequestParam(required = false)
            Integer visitedAt
        ) {
        SearchCondition cond = new SearchCondition();
        cond.put("diaryId", String.valueOf(diaryId));

        if (visitedAt != null) {
            cond.put("visitedAt", String.valueOf(visitedAt));
        }

        return ResponseEntity.ok(routeService.list(cond));
    }


    @Operation(
            summary = "루트 수정",
            description = """
                • <code>visitedAt</code>, <code>orderIdx</code>, <code>attractionNo</code> 중 필요한 필드만 전송해 부분 수정이 가능합니다.  
                • <code>orderIdx</code> 를 변경하면 같은 다이어리 내 순서를 재배치해야 합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 id / attraction", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "루트 PK", required = true, example = "10")
            @PathVariable Long id,

            @Valid @RequestBody
            @Parameter(description = "루트 수정 요청 본문", required = true)
            RouteUpdateRequest req
    ) {
        routeService.update(id, req);
        return ResponseEntity.ok().build();
    }


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
        • 프론트에서 새 순서대로 정렬한 <code>routeIds</code> 와
          각 구간 <code>distances</code> 를 JSON 한 덩어리로 전송합니다.  
        • 응답은 200 OK
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재정렬 성공"),
            @ApiResponse(responseCode = "400", description = "리스트 비어 있음 / 서로 다른 diaryId 포함", content = @Content)
    })
    public ResponseEntity<Void> reorder(
            @Valid @RequestBody RouteReorderRequest req) {

        List<Long> ids  = req.routeIds();
        List<Double> ds = req.distances();

        int order = 1;
        for (int i = 0; i < ids.size() - 1; i++) {
            routeService.update(ids.get(i),
                    new RouteUpdateRequest(null, order++, null, ds.get(i)));
        }

        // 마지막 루트: distance = null
        routeService.update(ids.get(ids.size() - 1),
                new RouteUpdateRequest(null, order, null, null));

        return ResponseEntity.ok().build();
    }
}
