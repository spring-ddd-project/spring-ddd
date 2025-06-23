package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
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
        if (!ObjectUtils.isEmpty(deptId)) {
            super.setDeptId(deptId);
        }
    }

    public void delete() {
        super.setDeleteStatus("1");
    }

    public void updateUserStatus(String status) {
        this.account.setLockStatus(status);
    }
}
