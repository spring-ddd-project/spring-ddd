package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.dept.exception.DeptIdNullException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDomain extends AbstractDomainMask {

    private UserId userId;

    private Account account;

    private ExtendInfo extendInfo;

    public void create() {}

    public void updateUser(Account newAccount, ExtendInfo newExtendInfo, Long deptId) {
        this.account = newAccount;
        this.extendInfo = newExtendInfo;
        if (ObjectUtils.isEmpty(deptId)) {
            throw new DeptIdNullException();
        }
        super.setDeptId(deptId);
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void updateUserStatus(Boolean status) {
        this.account.setLockStatus(status);
    }
}
