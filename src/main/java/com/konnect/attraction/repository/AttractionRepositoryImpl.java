package com.konnect.attraction.repository;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.entity.*;
import com.konnect.util.CursorPage;
import com.konnect.util.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttractionRepositoryImpl implements AttractionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public CursorPage<AttractionDTO> searchWithCondition(CursorPage<?> page, SearchCondition condition) {
        QAttraction attraction = QAttraction.attraction;
        QContentType contentType = QContentType.contentType;
        QSido sido = QSido.sido;
        QGugun gugun = QGugun.gugun;

        BooleanBuilder where = new BooleanBuilder();

        Long cursorId = page.getCursorIdOrDefault();
        where.and(attraction.no.lt(cursorId == Long.MAX_VALUE ? Integer.MAX_VALUE : cursorId));

        // Search 조건: title
        if (condition.has("title")) {
            where.and(attraction.title.containsIgnoreCase(condition.get("title")));
        }

        // Search 조건: contentTypeId or contentTypeName
        if (condition.has("contentTypeId")) {
            try {
                Integer contentTypeId = Integer.parseInt(condition.get("contentTypeId"));
                where.and(attraction.contentType.contentTypeId.eq(contentTypeId));
            } catch (NumberFormatException ignored) {}
        }

        // 조회 + 조인 + DTO 매핑
        List<AttractionDTO> results = queryFactory
                .select(Projections.fields(
                        AttractionDTO.class,
                        attraction.no,
                        attraction.contentId,
                        attraction.title,
                        attraction.contentType.contentTypeId.as("contentTypeId"),
                        attraction.contentType.contentTypeName.as("contentTypeName"),
                        attraction.sido.sidoCode.as("areaCode"),
                        attraction.sido.sidoName.as("sidoName"),
                        attraction.gugun.gugunCode.as("gugunCode"),
                        attraction.gugun.gugunName.as("gugunName"),
                        attraction.firstImage1,
                        attraction.firstImage2,
                        attraction.mapLevel,
                        attraction.latitude,
                        attraction.longitude,
                        attraction.tel,
                        attraction.addr1,
                        attraction.addr2,
                        attraction.homepage,
                        attraction.overview
                ))
                .from(attraction)
                .leftJoin(attraction.contentType, contentType)
                .leftJoin(attraction.sido, sido)
                .join(attraction.gugun, gugun)
//                .on(gugun.sido.sidoCode.eq(attraction.sido.sidoCode)
//                    .and(gugun.gugunCode.eq(attraction.gugun.gugunCode)))
                .where(where)
                .orderBy(attraction.no.desc())
                .limit(page.getSize() + 1)
                .fetch();

        return CursorPage.of(results, page.getSize());
    }

    @Override
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
                        attraction.contentType.contentTypeId.as("contentTypeId"),
                        attraction.contentType.contentTypeName.as("contentTypeName"),
                        attraction.sido.sidoCode.as("areaCode"),
                        attraction.sido.sidoName.as("sidoName"),
                        attraction.gugun.gugunCode.as("gugunCode"),
                        attraction.gugun.gugunName.as("gugunName"),
                        attraction.firstImage1,
                        attraction.firstImage2,
                        attraction.mapLevel,
                        attraction.latitude,
                        attraction.longitude,
                        attraction.tel,
                        attraction.addr1,
                        attraction.addr2,
                        attraction.homepage,
                        attraction.overview
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

