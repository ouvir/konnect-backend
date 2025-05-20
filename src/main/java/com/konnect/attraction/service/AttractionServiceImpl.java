package com.konnect.attraction.service;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.repository.AttractionRepository;
import com.konnect.util.CursorPage;
import com.konnect.util.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository attractionRepository;

    public CursorPage<AttractionDTO> searchAttractions(
            Long cursorId,
            int size,
            SearchCondition condition
    ) {
        CursorPage<?> page = new CursorPage<>(cursorId, size);
        return attractionRepository.searchWithCondition(page, condition);
    }

    @Override
    public AttractionDTO getAttractionDetail(Integer id) {
        return attractionRepository.findByNo(id);
    }
}
