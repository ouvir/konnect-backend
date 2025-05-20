package com.konnect.attraction.service;

import com.konnect.attraction.dto.AttractionDTO;
import com.konnect.attraction.repository.AttractionRepository;
import com.konnect.util.CursorPage;
import com.konnect.util.OffsetPage;
import com.konnect.util.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository attractionRepository;

    @Override
    public OffsetPage<AttractionDTO> searchAttractions(int page, int size, SearchCondition condition) {
        return attractionRepository.searchWithCondition(page, size, condition);
    }

    @Override
    public AttractionDTO getAttractionDetail(Integer id) {
        return attractionRepository.findByNo(id);
    }
}
