package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.FilterComponentNullException;
import com.springddd.domain.gen.exception.FilterNullException;
import com.springddd.domain.gen.exception.OrderNullException;
import com.springddd.domain.gen.exception.VisibleNullException;
import org.springframework.util.ObjectUtils;

public record Table(Boolean tableVisible, Boolean tableOrder, Boolean tableFilter, Byte tableFilterComponent, Byte tableFilterType) {

    public Table {
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
    }
}
