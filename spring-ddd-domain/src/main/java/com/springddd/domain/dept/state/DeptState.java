package com.springddd.domain.dept.state;

import com.springddd.domain.dept.SysDeptDomain;

public interface DeptState {
    void delete(SysDeptDomain domain);
    void restore(SysDeptDomain domain);
}















