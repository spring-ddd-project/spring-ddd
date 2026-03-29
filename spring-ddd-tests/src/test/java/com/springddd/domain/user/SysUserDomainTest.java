package com.springddd.domain.user;

import com.springddd.domain.dept.exception.DeptIdNullException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysUserDomainTest {

    @Test
    void create_shouldInitializeDomain() {
        SysUserDomain domain = new SysUserDomain();
        domain.setId(new UserId(1L));
        domain.setUserId(new UserId(1L));
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("password"));
        domain.setAccount(account);
        ExtendInfo extendInfo = new ExtendInfo();
        domain.setExtendInfo(extendInfo);
        domain.create();

        assertNotNull(domain);
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void updateUser_shouldModifyAccountAndExtendInfo() {
        SysUserDomain domain = new SysUserDomain();
        domain.setId(new UserId(1L));
        domain.setUserId(new UserId(1L));

        Account newAccount = new Account();
        newAccount.setUsername(new Username("newuser"));
        newAccount.setPassword(new Password("newpassword"));

        ExtendInfo newExtendInfo = new ExtendInfo();
        newExtendInfo.setAvatar("/new/avatar.png");
        newExtendInfo.setSex(true);

        domain.updateUser(newAccount, newExtendInfo, 100L);

        assertEquals(newAccount, domain.getAccount());
        assertEquals(newExtendInfo, domain.getExtendInfo());
        assertEquals(100L, domain.getDeptId());
    }

    @Test
    void updateUser_shouldThrowDeptIdNullExceptionWhenDeptIdIsNull() {
        SysUserDomain domain = new SysUserDomain();
        domain.setId(new UserId(1L));
        domain.setUserId(new UserId(1L));

        Account newAccount = new Account();
        newAccount.setUsername(new Username("newuser"));
        newAccount.setPassword(new Password("newpassword"));

        ExtendInfo newExtendInfo = new ExtendInfo();

        assertThrows(DeptIdNullException.class, () -> domain.updateUser(newAccount, newExtendInfo, null));
    }

    @Test
    void updateUser_shouldThrowDeptIdNullExceptionWhenDeptIdIsZero() {
        SysUserDomain domain = new SysUserDomain();
        domain.setId(new UserId(1L));
        domain.setUserId(new UserId(1L));

        Account newAccount = new Account();
        newAccount.setUsername(new Username("newuser"));
        newAccount.setPassword(new Password("newpassword"));

        ExtendInfo newExtendInfo = new ExtendInfo();

        assertThrows(DeptIdNullException.class, () -> domain.updateUser(newAccount, newExtendInfo, 0L));
    }

    @Test
    void delete_shouldSetDeleteStatus() {
        SysUserDomain domain = new SysUserDomain();
        assertFalse(domain.getDeleteStatus());

        domain.delete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldClearDeleteStatus() {
        SysUserDomain domain = new SysUserDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());

        domain.restore();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void updateUserStatus_shouldSetLockStatus() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setLockStatus(false);
        domain.setAccount(account);

        domain.updateUserStatus(true);

        assertTrue(domain.getAccount().getLockStatus());
    }

    @Test
    void setId_and_getId_shouldWork() {
        SysUserDomain domain = new SysUserDomain();
        UserId id = new UserId(100L);
        domain.setId(id);
        assertEquals(id, domain.getId());
    }

    @Test
    void setUserId_and_getUserId_shouldWork() {
        SysUserDomain domain = new SysUserDomain();
        UserId userId = new UserId(50L);
        domain.setUserId(userId);
        assertEquals(userId, domain.getUserId());
    }

    @Test
    void setAccount_and_getAccount_shouldWork() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setUsername(new Username("admin"));
        account.setPassword(new Password("admin123"));
        domain.setAccount(account);
        assertEquals(account, domain.getAccount());
    }

    @Test
    void setExtendInfo_and_getExtendInfo_shouldWork() {
        SysUserDomain domain = new SysUserDomain();
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("/avatar.png");
        domain.setExtendInfo(extendInfo);
        assertEquals(extendInfo, domain.getExtendInfo());
    }

    @Test
    void setDeptId_and_getDeptId_shouldWork() {
        SysUserDomain domain = new SysUserDomain();
        domain.setDeptId(200L);
        assertEquals(200L, domain.getDeptId());
    }
}
