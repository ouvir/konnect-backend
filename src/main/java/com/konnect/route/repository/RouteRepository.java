package com.konnect.route.repository;

import com.konnect.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long>, RouteRepositoryCustom {
    /** 특정 다이어리의 루트를 방문 일자·시간 오름차순으로 반환 */
    List<Route> findByDiaryDiaryIdOrderByVisitedDateAscVisitedTimeAsc(Long diaryId);
    void deleteByDiaryDiaryId(Long diaryId);
    List<Route> findAllByDiary_DiaryIdOrderByVisitedDateAscVisitedTimeAsc(Long diaryId);
}