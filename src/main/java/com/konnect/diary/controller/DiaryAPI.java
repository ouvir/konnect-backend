package com.konnect.diary.controller;

import com.konnect.auth.dto.CustomUserPrincipal;
import com.konnect.diary.dto.CreateDiaryDraftRequestDTO;
import com.konnect.diary.dto.CreateDiaryResponseDTO;
import com.konnect.diary.dto.DiarySortType;
import com.konnect.diary.dto.ListDiaryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "다이어리", description = "다이어리 관련 API")
public interface DiaryAPI {
    @Operation(
            summary = "임시저장 다이어리 생성/수정",
            description = "드래프트(draft) 상태인 다이어리를 생성하거나 업데이트합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "드래프트 수정 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateDiaryResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "201",
                    description = "드래프트 생성 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateDiaryResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<CreateDiaryResponseDTO> saveDraft(
            @Parameter(
                    description = "드래프트 저장 요청 DTO",
                    required = true,
                    schema = @Schema(implementation = CreateDiaryDraftRequestDTO.class)
            )
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,

            @Parameter(
                    description = "썸네일 이미지 파일 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,

            @Parameter(
                    description = "본문 이미지 파일 목록, 최대 9장 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,

            @AuthenticationPrincipal CustomUserPrincipal userDetails
    );

    public ResponseEntity<CreateDiaryResponseDTO> publishDiary(
            @PathVariable Long diaryId,

            @Parameter(
                    description = "다이어리 게시 요청 DTO",
                    required = true,
                    schema = @Schema(implementation = CreateDiaryDraftRequestDTO.class)
            )
            @RequestPart("data") CreateDiaryDraftRequestDTO requestDTO,

            @Parameter(
                    description = "썸네일 이미지 파일 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,

            @Parameter(
                    description = "본문 이미지 파일 목록, 최대 9장 (optional)",
                    in = ParameterIn.HEADER,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,

            @AuthenticationPrincipal CustomUserPrincipal userDetails
    );

    @Operation(
            summary     = "다이어리 목록 조회",
            description = "특정 지역(areaId)에 속한 게시된 다이어리를 조회합니다. " +
                    "topOnly=true인 경우 상위 4개, false인 경우 전체를 반환합니다. " +
                    "sortedBy로 정렬 기준을 지정하세요."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "조회 성공",
                    content      = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ListDiaryResponseDTO.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "해당 지역의 다이어리가 없음")
    })
    public ResponseEntity<List<ListDiaryResponseDTO>> fetchDiaries(
            @Parameter(
                    description = "조회할 지역 ID",
                    required    = true,
                    schema      = @Schema(type = "integer", example = "42")
            )
            @RequestParam(name = "areaId") Long areaId,

            @Parameter(
                    description = "상위 4개만 조회할지 여부",
                    schema      = @Schema(type = "boolean", defaultValue = "true")
            )
            @RequestParam(name = "topOnly", defaultValue = "true") boolean topOnly,

            @Parameter(
                    description = "정렬 기준 (RECENT, MOST_LIKED)",
                    schema      = @Schema(
                            implementation = DiarySortType.class,
                            defaultValue   = "MOST_LIKED"
                    )
            )
            @RequestParam(name = "sortedBy", defaultValue = "MOST_LIKED") DiarySortType sortedBy
    );
    @Operation(
            summary     = "마이페이지 다이어리 목록 조회",
            description = "내 다이어리 목록을 조회합니다. " +
                    "수정 중인 다이어리가 가장 최상단에 불러와지며, " +
                    "그 다음 다이어리들은 최신순으로 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "조회 성공",
                    content      = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ListDiaryResponseDTO.class)
                            )
                    )
            ),
    })
    public ResponseEntity<List<ListDiaryResponseDTO>> fetchMyDiaries(
            @AuthenticationPrincipal CustomUserPrincipal userDetails
    );
}
