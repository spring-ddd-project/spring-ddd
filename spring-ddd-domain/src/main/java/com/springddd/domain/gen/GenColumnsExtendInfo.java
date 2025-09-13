package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.springframework.util.ObjectUtils;

public record GenColumnsExtendInfo(Long propDictId,
                                   Byte typescriptType,
                                   Byte formComponent,
                                   Boolean formVisible,
                                   Boolean formRequired) {
    public GenColumnsExtendInfo {
        if (ObjectUtils.isEmpty(formComponent)) {
            throw new FormComponentNullException();
        }
        if (ObjectUtils.isEmpty(formVisible)) {
            throw new FormVisibleNullException();
        }
        if (ObjectUtils.isEmpty(formRequired)) {
            throw new FormRequiredNullException();
        }
    }
}
