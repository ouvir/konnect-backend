package com.konnect.attraction.repository;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.entity.*;
import com.konnect.util.CursorPage;
import com.konnect.util.OffsetPage;
import com.konnect.util.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttractionRepositoryImpl implements AttractionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public OffsetPage<AttractionDTO> searchWithCondition(int page, int size, SearchCondition condition) {
        QAttraction attraction = QAttraction.attraction;
        BooleanBuilder where = new BooleanBuilder();

        if (condition.has("title")) {
            where.and(attraction.title.containsIgnoreCase(condition.get("title")));
        }
        if (condition.has("area")) {
            String areaText = condition.get("area");
            if(areaText.equals("기타")) {
                // '서울', '전주', '부산', '제주', '여수' 가 포함되지 않은 행만 선택
                BooleanBuilder etc = new BooleanBuilder()
                        .and(attraction.addr1.containsIgnoreCase("서울").not())
                        .and(attraction.sido.sidoName.containsIgnoreCase("서울").not())

                        .and(attraction.addr1.containsIgnoreCase("전주").not())
                        .and(attraction.gugun.gugunName.containsIgnoreCase("전주").not())

                        .and(attraction.addr1.containsIgnoreCase("부산").not())
                        .and(attraction.sido.sidoName.containsIgnoreCase("부산").not())

                        .and(attraction.addr1.containsIgnoreCase("제주").not())
                        .and(attraction.sido.sidoName.containsIgnoreCase("제주").not())
                        .and(attraction.gugun.gugunName.containsIgnoreCase("제주").not())

                        .and(attraction.addr1.containsIgnoreCase("여수").not())
                        .and(attraction.gugun.gugunName.containsIgnoreCase("여수").not());

                where.and(etc);
            } else {
                where.and(attraction.sido.sidoName.containsIgnoreCase(areaText)
                        .or(attraction.gugun.gugunName.containsIgnoreCase(areaText))
                );
            }
        }
        if (condition.has("areaCode")) {
            Integer areaCode = Integer.parseInt(condition.get("areaCode"));
            where.and(attraction.sido.sidoCode.eq(areaCode));
        }
        if (condition.has("contentTypeId")) {
            try {
                Integer contentTypeId = Integer.parseInt(condition.get("contentTypeId"));
                where.and(attraction.contentType.contentTypeId.eq(contentTypeId));
            } catch (NumberFormatException ignored) {}
        }

        long total = queryFactory
                .select(attraction.count())
                .from(attraction)
                .where(where)
                .fetchOne();

        List<AttractionDTO> results = queryFactory
                .select(Projections.fields(
                        AttractionDTO.class,
                        attraction.no,
                        attraction.contentId,
                        attraction.title,
                        attraction.titleEng,
                        attraction.contentType.contentTypeId.as("contentTypeId"),
                        attraction.contentType.contentTypeName.as("contentTypeName"),
                        attraction.contentType.contentTypeNameEng.as("contentTypeNameEng"),
                        attraction.sido.sidoCode.as("areaCode"),
                        attraction.sido.sidoName.as("sidoName"),
                        attraction.sido.sidoNameEng.as("sidoNameEng"),
                        attraction.gugun.gugunCode.as("gugunCode"),
                        attraction.gugun.gugunName.as("gugunName"),
                        attraction.gugun.gugunNameEng.as("gugunNameEng"),
                        attraction.firstImage1,
                        attraction.firstImage2,
                        attraction.mapLevel,
                        attraction.latitude,
                        attraction.longitude,
                        attraction.tel,
                        attraction.addr1,
                        attraction.addr2,
                        attraction.homepage,
                        attraction.overview,
                        attraction.overviewEng
                ))
                .from(attraction)
                .leftJoin(attraction.contentType, QContentType.contentType)
                .leftJoin(attraction.sido, QSido.sido)
                .join(attraction.gugun, QGugun.gugun)
                .where(where)
                .orderBy(attraction.no.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        return OffsetPage.of(results, page, size, (int) total);
    }

    @Override
    @Transactional(readOnly = true)
    public AttractionDTO findByNo(Integer no) {
        QAttraction attraction = QAttraction.attraction;
        QContentType contentType = QContentType.contentType;
        QSido sido = QSido.sido;
        QGugun gugun = QGugun.gugun;

        BooleanBuilder where = new BooleanBuilder();
        where.and(attraction.no.eq(no));

        // 조회 + 조인 + DTO 매핑
        AttractionDTO result = queryFactory
                .select(Projections.fields(
                        AttractionDTO.class,
                        attraction.no,
                        attraction.contentId,
                        attraction.title,
                        attraction.titleEng,
                        attraction.contentType.contentTypeId.as("contentTypeId"),
                        attraction.contentType.contentTypeName.as("contentTypeName"),
                        attraction.contentType.contentTypeNameEng.as("contentTypeNameEng"),
                        attraction.sido.sidoCode.as("areaCode"),
                        attraction.sido.sidoName.as("sidoName"),
                        attraction.sido.sidoNameEng.as("sidoNameEng"),
                        attraction.gugun.gugunCode.as("gugunCode"),
                        attraction.gugun.gugunName.as("gugunName"),
                        attraction.gugun.gugunNameEng.as("gugunNameEng"),
                        attraction.firstImage1,
                        attraction.firstImage2,
                        attraction.mapLevel,
                        attraction.latitude,
                        attraction.longitude,
                        attraction.tel,
                        attraction.addr1,
                        attraction.addr2,
                        attraction.homepage,
                        attraction.overview,
                        attraction.overviewEng
                ))
                .from(attraction)
                .leftJoin(attraction.contentType, contentType)
                .leftJoin(attraction.sido, sido)
                .join(attraction.gugun, gugun)
//                .on(gugun.sido.sidoCode.eq(attraction.sido.sidoCode)
//                        .and(gugun.gugunCode.eq(attraction.gugun.gugunCode)))
                .where(where)
                .fetchOne();
        return result;
    }

}

