package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.dept.exception.DeptIdNullException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDomain extends AbstractDomainMask implements Cloneable {

    private UserId userId;

    private Account account;

    private ExtendInfo extendInfo;

    @Override
    public SysUserDomain clone() {
        try {
            SysUserDomain clone = (SysUserDomain) super.clone();
            if (this.userId != null) clone.setUserId(new UserId(this.userId.value()));
            if (this.account != null) {
                Account acc = new Account();
                acc.setUsername(this.account.getUsername());
                acc.setPassword(this.account.getPassword());
                acc.setLockStatus(this.account.getLockStatus());
                clone.setAccount(acc);
            }
            if (this.extendInfo != null) {
                ExtendInfo ext = new ExtendInfo();
                ext.setNickName(this.extendInfo.getNickName());
                ext.setAvatar(this.extendInfo.getAvatar());
                ext.setEmail(this.extendInfo.getEmail());
                ext.setPhone(this.extendInfo.getPhone());
                ext.setGender(this.extendInfo.getGender());
                clone.setExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.user.state.UserState userState;

    public void setState(com.springddd.domain.user.state.UserState state) {
        this.userState = state;
    }

    public void create() {
        this.userState = new com.springddd.domain.user.state.NormalState();
    }

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

    public void restore() {
        super.setDeleteStatus(false);
    }

    public void lock() {
        if (userState == null) userState = account.getLockStatus() ? new com.springddd.domain.user.state.LockedState() : new com.springddd.domain.user.state.NormalState();
        userState.lock(this);
    }

    public void unlock() {
        if (userState == null) userState = account.getLockStatus() ? new com.springddd.domain.user.state.LockedState() : new com.springddd.domain.user.state.NormalState();
        userState.unlock(this);
    }

    public void updateUserStatus(Boolean status) {
        if (status) lock();
        else unlock();
    }
}
