package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

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
        super.setUpdateTime(LocalDateTime.now());
    }

    public void delete() {
        super.setDeleteStatus("1");
        super.setUpdateTime(LocalDateTime.now());
    }

    public void updateUserStatus(String status, String updateBy) {
        this.account.setLockStatus(status);
        super.setUpdateBy(updateBy);
        super.setUpdateTime(LocalDateTime.now());
    }
}
