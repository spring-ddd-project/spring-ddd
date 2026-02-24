package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptStatusNullException;
import com.springddd.domain.dept.exception.SortOrderNullException;
import org.springframework.util.ObjectUtils;

public record DeptExtendInfo(Integer sortOrder, Boolean deptStatus) {

    public DeptExtendInfo {
        if (ObjectUtils.isEmpty(sortOrder)) {
            throw new SortOrderNullException();
        }
        if (ObjectUtils.isEmpty(deptStatus)) {
            throw new DeptStatusNullException();
        }
    }
}



































