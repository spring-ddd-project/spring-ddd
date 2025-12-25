package com.springddd.domain.dept.state;

import com.springddd.domain.dept.SysDeptDomain;

public class DeletedDeptState implements DeptState {
    @Override
    public void delete(SysDeptDomain domain) {
        // Already deleted
    }

    @Override
    public void restore(SysDeptDomain domain) {
        domain.setDeleteStatus(false);
        domain.setState(new ActiveDeptState());
    }
}



























