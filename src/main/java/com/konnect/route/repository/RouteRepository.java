package com.konnect.route.repository;

import com.konnect.route.dto.RouteResponse;
import com.konnect.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long>, RouteRepositoryCustom {
    List<Route> findByDiaryDiaryIdOrderByOrderIdxAsc(Long diaryId);
}

