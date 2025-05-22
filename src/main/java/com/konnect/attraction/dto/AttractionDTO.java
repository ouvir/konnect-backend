package com.konnect.attraction.dto;

import com.konnect.util.CursorPage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "관광지(Attraction) 정보 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDTO implements CursorPage.Identifiable {
    @Id
    @Schema(description = "관광지 고유 ID")
    private Integer no;

    @Schema(description = "콘텐츠 ID")
    private Integer contentId;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "제목(영문)")
    private String titleEng;

    @Schema(description = "콘텐츠 타입 ID")
    private Integer contentTypeId;

    @Schema(description = "콘텐츠 타입 이름")
    private String contentTypeName;

    @Schema(description = "콘텐츠 타입 이름(영문)")
    private String contentTypeNameEng;


    @Schema(description = "시도 코드")
    private Integer areaCode;

    @Schema(description = "시도 이름")
    private String sidoName;

    @Schema(description = "시도 이름(영문)")
    private String sidoNameEng;

    @Schema(description = "구군 코드")
    private Integer gugunCode;

    @Schema(description = "구군 이름")
    private String gugunName;

    @Schema(description = "구군 이름(영문)")
    private String gugunNameEng;

    @Schema(description = "대표 이미지 1")
    private String firstImage1;

    @Schema(description = "대표 이미지 2")
    private String firstImage2;

    @Schema(description = "지도 확대 레벨")
    private Integer mapLevel;

    @Schema(description = "위도")
    private BigDecimal latitude;

    @Schema(description = "경도")
    private BigDecimal longitude;

    @Schema(description = "전화번호")
    private String tel;

    @Schema(description = "주소 1")
    private String addr1;

    @Schema(description = "주소 2")
    private String addr2;

    @Schema(description = "홈페이지 URL")
    private String homepage;

    @Schema(description = "설명")
    private String overview;

    @Schema(description = "설명(영문)")
    private String overviewEng;

    @Override
    public Long getId() {
        return no != null ? no.longValue() : null;
    }
}

