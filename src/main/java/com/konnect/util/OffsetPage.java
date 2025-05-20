package com.konnect.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OffsetPage<T> {
    // 요청용
    private int page;
    private int size;

    // 응답용
    private List<T> content;
    private int totalElements;
    private int totalPages;
    private boolean hasNext;

    public OffsetPage(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public static <T> OffsetPage<T> of(List<T> content, int page, int size, int totalElements) {
        OffsetPage<T> result = new OffsetPage<>(page, size);
        result.setContent(content);
        result.setTotalElements(totalElements);
        result.setTotalPages((int) Math.ceil((double) totalElements / size));
        result.setHasNext(page * size + content.size() < totalElements);
        return result;
    }
}