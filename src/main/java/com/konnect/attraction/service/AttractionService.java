package com.konnect.attraction.service;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.util.CursorPage;
import com.konnect.util.SearchCondition;

public interface AttractionService {
    CursorPage<AttractionDTO> searchAttractions(Long cursorId, int size, SearchCondition condition);
    AttractionDTO getAttractionDetail(Integer id);
}
