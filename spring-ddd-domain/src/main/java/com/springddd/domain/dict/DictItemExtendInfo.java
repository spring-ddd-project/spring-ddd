package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemItemStatusNullException;
import com.springddd.domain.dict.exception.DictItemSortOrderNullException;
import org.springframework.util.ObjectUtils;

public record DictItemExtendInfo(Integer sortOrder, Boolean itemStatus) {

    public DictItemExtendInfo {
        if (ObjectUtils.isEmpty(sortOrder)) {
            throw new DictItemSortOrderNullException();
        }
        if (ObjectUtils.isEmpty(itemStatus)) {
            throw new DictItemItemStatusNullException();
        }
    }
}
