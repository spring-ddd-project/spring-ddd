package com.springddd.domain.dept.state;

import com.springddd.domain.dept.SysDeptDomain;

public class ActiveDeptState implements DeptState {
    @Override
    public void delete(SysDeptDomain domain) {
        domain.setDeleteStatus(true);
        domain.setState(new DeletedDeptState());
    }

    @Override
    public void restore(SysDeptDomain domain) {
        // Already active
    }
}
























