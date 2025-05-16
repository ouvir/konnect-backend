package com.konnect.attraction.repository;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.util.CursorPage;
import com.konnect.util.SearchCondition;

public interface AttractionRepositoryCustom {
    CursorPage<AttractionDTO> searchWithCondition(CursorPage<?> page, SearchCondition condition);
}
