package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysUserDomainTest {

    @Test
    void shouldCreateSysUserDomain() {
        SysUserDomain domain = new SysUserDomain();
        assertNotNull(domain);
    }

    @Test
    void shouldSetAndGetId() {
        SysUserDomain domain = new SysUserDomain();
        UserId id = new UserId(1L);
        domain.setUserId(id);
        assertEquals(id, domain.getUserId());
    }

    @Test
    void shouldSetAndGetAccount() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setEmail("test@example.com");
        domain.setAccount(account);
        assertEquals("test@example.com", domain.getAccount().getEmail());
    }

    @Test
    void shouldCallCreate() {
        SysUserDomain domain = new SysUserDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void shouldUpdateUser() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setUsername(new Username("user1"));
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");

        domain.updateUser(account, extendInfo, 1L);

        assertEquals(account, domain.getAccount());
        assertEquals(extendInfo, domain.getExtendInfo());
        assertEquals(1L, domain.getDeptId());
    }

    @Test
    void shouldThrowWhenUpdateUserWithNullDeptId() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        ExtendInfo extendInfo = new ExtendInfo();

        assertThrows(com.springddd.domain.dept.exception.DeptIdNullException.class, () -> {
            domain.updateUser(account, extendInfo, null);
        });
    }

    @Test
    void shouldUpdateUserStatus() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account();
        account.setLockStatus(false);
        domain.setAccount(account);

        domain.updateUserStatus(true);

        assertTrue(domain.getAccount().getLockStatus());
    }

    @Test
    void shouldSetAndGetDeleteStatus() {
        SysUserDomain domain = new SysUserDomain();
        domain.setDeleteStatus(false);
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void shouldClearDeleteStatusOnRestore() {
        SysUserDomain domain = new SysUserDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void toString_shouldReturnValueAsString() {
        SysUserDomain domain = new SysUserDomain();
        String str = domain.toString();
        assertTrue(str.contains("SysUserDomain"));
    }
}
