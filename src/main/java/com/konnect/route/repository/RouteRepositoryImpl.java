package com.konnect.route.repository;

import com.konnect.route.dto.RouteDetailResponse;
import com.konnect.route.entity.QRoute;
import com.konnect.util.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory query;

    /* ---------- 목록 조회 ---------- */
    @Override
    public List<RouteDetailResponse> searchByDiary(Long diaryId, SearchCondition cond) {
        QRoute r = QRoute.route;

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
                        r.title,
                        r.latitude,
                        r.longitude
                ))
                .from(r)
                .where(where)
                .orderBy(r.visitedDate.asc(), r.visitedTime.asc())
                .fetch();
    }

    /* ---------- 단일 상세 ---------- */
    @Override
    public Optional<RouteDetailResponse> findDetailById(Long routeId) {
        QRoute r = QRoute.route;

        RouteDetailResponse dto = query
                .select(Projections.constructor(RouteDetailResponse.class,
                        r.id,
                        r.visitedDate,
                        r.visitedTime,
                        r.distance,
                        r.title,
                        r.latitude,
                        r.longitude
                ))
                .from(r)
                .where(r.id.eq(routeId))
                .fetchOne();

        return Optional.ofNullable(dto);
    }
}