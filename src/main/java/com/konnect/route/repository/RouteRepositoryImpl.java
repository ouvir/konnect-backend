package com.konnect.route.repository;

import com.konnect.attraction.entity.QAttraction;
import com.konnect.route.dto.RouteResponse;
import com.konnect.route.entity.QRoute;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public int nextOrderIdx(Long diaryId) {
        QRoute route = QRoute.route;
        Integer max = query.select(route.orderIdx.max())
                .from(route)
                .where(route.diary.diaryId.eq(diaryId))
                .fetchOne();
        return max == null ? 1 : max + 1;
    }

    @Override
    public List<RouteResponse> searchByDiary(Long diaryId) {
        QRoute r = QRoute.route;
        QAttraction a = QAttraction.attraction;

        return query
                .select(Projections.constructor(RouteResponse.class,
                        r.id,
                        r.orderIdx,
                        r.visitedAt,
                        a.no,
                        a.title
                ))
                .from(r)
                .join(r.attraction, a)
                .where(r.diary.diaryId.eq(diaryId))
                .orderBy(r.orderIdx.asc())
                .fetch();
    }
}
