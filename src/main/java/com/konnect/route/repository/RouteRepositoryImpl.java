package com.konnect.route.repository;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.entity.QAttraction;
import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.route.entity.QRoute;
import com.konnect.util.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public int nextOrderIdx(Long diaryId, Integer visitedAt) {
        QRoute route = QRoute.route;
        Integer max = query.select(route.orderIdx.max())
                .from(route)
                .where(route.diary.diaryId.eq(diaryId).and(route.visitedAt.eq(visitedAt)))
                .fetchOne();
        return max == null ? 1 : max + 1;
    }

    private Expression<AttractionDTO> attractionProjection(QAttraction a) {
        // AttractionDTO(생성자) 파라미터 순서는 클래스 생성자와 동일해야 합니다.
        return Projections.constructor(AttractionDTO.class,
                a.no,
                a.contentId,
                a.title,
                a.contentType.contentTypeId,
                a.contentType.contentTypeName,
                a.sido.sidoCode,
                a.sido.sidoName,
                a.gugun.gugunCode,
                a.gugun.gugunName,
                a.firstImage1,
                a.firstImage2,
                a.mapLevel,
                a.latitude,
                a.longitude,
                a.tel,
                a.addr1,
                a.addr2,
                a.homepage,
                a.overview
        );
    }

    @Override
    public List<RouteDetailResponse> searchByDiary(Long diaryId, SearchCondition condition) {
        QRoute r = QRoute.route;
        QAttraction a = QAttraction.attraction;

        BooleanBuilder where = new BooleanBuilder();
        where.and(r.diary.diaryId.eq(diaryId));

        if (condition.has("visitedAt")) {
            Integer visitedAt = Integer.parseInt(condition.get("visitedAt"));
            where.and(r.visitedAt.eq(visitedAt));
        }

        return query
                .select(Projections.constructor(RouteDetailResponse.class,
                        r.id,
                        r.orderIdx,
                        r.visitedAt,
                        r.distance,
                        attractionProjection(a)
                ))
                .from(r)
                .join(r.attraction, a)
                .where(where)
                .orderBy(r.orderIdx.asc())
                .fetch();
    }

    /** Route + Attraction 을 DTO 로 즉시 매핑 */
    @Override
    public Optional<RouteDetailResponse> findDetailById(Long routeId) {
        QAttraction attraction = QAttraction.attraction;
        QRoute route = QRoute.route;

        // AttractionDTO 서브-프로젝션
        var attractionDto = Projections.fields(
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
                attraction.addr2
        );

        RouteDetailResponse dto = query
                .select(Projections.constructor(
                        RouteDetailResponse.class,
                        route.id,
                        route.orderIdx,
                        route.visitedAt,
                        route.distance,          // ★ 추가
                        attractionDto           // ← 서브 DTO
                ))
                .from(route)
                .join(attraction).on(route.attraction.no.eq(attraction.no))
                .where(route.id.eq(routeId))
                .fetchOne();

        return Optional.ofNullable(dto);
    }
}
