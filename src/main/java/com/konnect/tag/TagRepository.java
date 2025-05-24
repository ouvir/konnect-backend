package com.konnect.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByNameIn(List<String> names);
    List<TagEntity> findByNameEngIn(List<String> names);
}
