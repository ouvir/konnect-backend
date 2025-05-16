package com.konnect.attraction.repository;

import com.konnect.attraction.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<Attraction, Integer>, AttractionRepositoryCustom {
}
