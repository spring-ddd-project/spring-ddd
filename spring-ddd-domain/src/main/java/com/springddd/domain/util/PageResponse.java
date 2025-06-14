package com.springddd.domain.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> items;
    private long total;
    private int pageNum;
    private int pageSize;

    public PageResponse(List<T> items, long total, int pageNum, int pageSize) {
        this.items = items;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}

