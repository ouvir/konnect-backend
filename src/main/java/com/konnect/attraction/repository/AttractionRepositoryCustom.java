package com.konnect.attraction.repository;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.util.OffsetPage;
import com.konnect.util.SearchCondition;

public interface AttractionRepositoryCustom {
    OffsetPage<AttractionDTO> searchWithCondition(int page, int size, SearchCondition condition);
    AttractionDTO findByNo(Integer no);
}
