package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.DomainMaskNullException;
import org.springframework.util.ObjectUtils;

public record GenAggregateExtendInfo(String domainMask) {

    public GenAggregateExtendInfo {
        if (ObjectUtils.isEmpty(domainMask)) {
            throw new DomainMaskNullException();
        }
    }
}
