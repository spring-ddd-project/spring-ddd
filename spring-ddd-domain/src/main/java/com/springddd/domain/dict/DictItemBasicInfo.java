package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictItemLabelNullException;
import com.springddd.domain.dict.exception.DictItemValueNullException;
import org.springframework.util.ObjectUtils;

public record DictItemBasicInfo(String itemLabel, Integer itemValue) {

    public DictItemBasicInfo {
        if (ObjectUtils.isEmpty(itemLabel)) {
            throw new DictItemLabelNullException();
        }
        if (ObjectUtils.isEmpty(itemValue)) {
            throw new DictItemValueNullException();
        }
    }
}
