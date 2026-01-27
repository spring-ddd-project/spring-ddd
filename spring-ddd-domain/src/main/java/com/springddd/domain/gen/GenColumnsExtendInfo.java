package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.springframework.util.ObjectUtils;

public record GenColumnsExtendInfo(Long propDictId,
                                   Boolean tableVisible,
                                   Boolean tableOrder,
                                   Boolean tableFilter,
                                   Integer tableFilterComponent,
                                   Integer tableFilterType,
                                   Integer formComponent,
                                   Boolean formVisible,
                                   Boolean formRequired) {
    public GenColumnsExtendInfo {
        if (ObjectUtils.isEmpty(tableVisible)) {
            throw new VisibleNullException();
        }
        if (ObjectUtils.isEmpty(tableOrder)) {
            throw new OrderNullException();
        }
        if (ObjectUtils.isEmpty(tableFilter)) {
            throw new FilterNullException();
        }
        if (tableFilter && ObjectUtils.isEmpty(tableFilterComponent)) {
            throw new FilterComponentNullException();
        }
        if (ObjectUtils.isEmpty(tableFilterType)) {
            throw new FilterTypeNullException();
        }
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
