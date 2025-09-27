package com.springddd.domain.dept;

import com.springddd.domain.dept.exception.DeptNameNullException;
import org.springframework.util.ObjectUtils;

public record DeptBasicInfo(String deptName) {

    public DeptBasicInfo {
        if (ObjectUtils.isEmpty(deptName)) {
            throw new DeptNameNullException();
        }
    }
}
