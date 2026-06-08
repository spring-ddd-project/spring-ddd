package com.springddd.domain.dict;

import com.springddd.domain.dict.exception.DictCodeNullException;
import com.springddd.domain.dict.exception.DictNameNullException;
import org.springframework.util.ObjectUtils;

public record DictBasicInfo(String dictName, String dictCode) {

    public DictBasicInfo {
        if (ObjectUtils.isEmpty(dictCode)) {
            throw new DictCodeNullException();
        }
        if (ObjectUtils.isEmpty(dictName)) {
            throw new DictNameNullException();
        }
    }
}
