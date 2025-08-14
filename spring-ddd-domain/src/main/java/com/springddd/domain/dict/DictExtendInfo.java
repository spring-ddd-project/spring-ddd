package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictDictStatusNullException;
import com.springddd.domain.dict.exception.DictSortOrderNullException;
import org.springframework.util.ObjectUtils;

public record DictExtendInfo(Integer sortOrder, Boolean dictStatus) {

    public DictExtendInfo {
        if (ObjectUtils.isEmpty(sortOrder)) {
            throw new DictSortOrderNullException();
        }
        if (ObjectUtils.isEmpty(dictStatus)) {
            throw new DictDictStatusNullException();
        }
    }
}
