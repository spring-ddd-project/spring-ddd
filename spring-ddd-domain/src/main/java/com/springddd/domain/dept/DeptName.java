package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptNameNullException;
import org.springframework.util.ObjectUtils;

public record DeptName(String value) {

    public DeptName {
        if (ObjectUtils.isEmpty(value)) {
            throw new DeptNameNullException();
        }
    }
}
