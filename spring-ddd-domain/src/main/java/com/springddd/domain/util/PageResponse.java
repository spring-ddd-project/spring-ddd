package com.springddd.domain.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> implements java.io.Serializable, Iterable<T> {

    private List<T> list;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;

    public PageResponse(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
































