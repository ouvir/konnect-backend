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
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory query;

    /* AttractionDTO 서브-프로젝션 재사용 */
    private Expression<AttractionDTO> attractionProjection(QAttraction a) {
        return Projections.constructor(AttractionDTO.class,
                a.no, a.contentId, a.title, a.titleEng,
                a.contentType.contentTypeId, a.contentType.contentTypeName, a.contentType.contentTypeNameEng,
                a.sido.sidoCode, a.sido.sidoName, a.sido.sidoNameEng,
                a.gugun.gugunCode, a.gugun.gugunName, a.gugun.gugunNameEng,
                a.firstImage1, a.firstImage2, a.mapLevel,
                a.latitude, a.longitude, a.tel,
                a.addr1, a.addr2, a.homepage, a.overview, a.overviewEng
        );
    }

    /* -------- 목록 조회 -------- */
    @Override
    public List<RouteDetailResponse> searchByDiary(Long diaryId, SearchCondition cond) {
        QRoute r = QRoute.route;
        QAttraction a = QAttraction.attraction;

        BooleanBuilder where = new BooleanBuilder()
                .and(r.diary.diaryId.eq(diaryId));

        if (cond.has("visitedDate")) {
            where.and(r.visitedDate.eq(cond.get("visitedDate")));
        }

        return query
                .select(Projections.constructor(RouteDetailResponse.class,
                        r.id,
                        r.visitedDate,
                        r.visitedTime,
                        r.distance,
                        attractionProjection(a)
                ))
                .from(r)
                .join(r.attraction, a)
                .where(where)
                .orderBy(r.visitedDate.asc(), r.visitedTime.asc())
                .fetch();
    }

    /* -------- 단일 상세 -------- */
    @Override
    public Optional<RouteDetailResponse> findDetailById(Long routeId) {
        QRoute r = QRoute.route;
        QAttraction a = QAttraction.attraction;

        RouteDetailResponse dto = query
                .select(Projections.constructor(RouteDetailResponse.class,
                        r.id,
                        r.visitedDate,
                        r.visitedTime,
                        r.distance,
                        attractionProjection(a)
                ))
                .from(r)
                .join(r.attraction, a)
                .where(r.id.eq(routeId))
                .fetchOne();

        return Optional.ofNullable(dto);
    }
}