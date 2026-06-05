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
        Account account = new Account(null, null, "test@example.com", null, false);
        domain.setAccount(account);
        assertEquals("test@example.com", domain.getAccount().email());
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
        Account account = new Account(new Username("user1"), new Password("pass"), null, null, false);
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
        Account account = new Account(null, null, null, null, false);
        ExtendInfo extendInfo = new ExtendInfo();

        assertThrows(com.springddd.domain.dept.exception.DeptIdNullException.class, () -> {
            domain.updateUser(account, extendInfo, null);
        });
    }

    @Test
    void shouldUpdateUserStatus() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account(null, null, null, null, false);
        domain.setAccount(account);

        domain.updateUserStatus(true);

        assertTrue(domain.getAccount().lockStatus());
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

    @Test
    void shouldLockWhenStateIsNullAndLockStatusTrue() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, true));

        domain.lock();

        assertNotNull(domain.getUserState());
    }

    @Test
    void shouldLockWhenStateIsNullAndLockStatusFalse() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, false));

        domain.lock();

        assertNotNull(domain.getUserState());
    }

    @Test
    void shouldLockWhenStateExists() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, false));
        domain.setState(new com.springddd.domain.user.state.LockedState());

        domain.lock();

        assertNotNull(domain.getUserState());
    }

    @Test
    void shouldUnlockWhenStateIsNullAndLockStatusTrue() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, true));

        domain.unlock();

        assertNotNull(domain.getUserState());
    }

    @Test
    void shouldUnlockWhenStateIsNullAndLockStatusFalse() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, false));

        domain.unlock();

        assertNotNull(domain.getUserState());
    }

    @Test
    void shouldUnlockWhenStateExists() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, false));
        domain.setState(new com.springddd.domain.user.state.NormalState());

        domain.unlock();

        assertNotNull(domain.getUserState());
    }

    @Test
    void shouldUpdateUserStatusWhenTrue() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, false));

        domain.updateUserStatus(true);

        assertTrue(domain.getAccount().lockStatus());
    }

    @Test
    void shouldUpdateUserStatusWhenFalse() {
        SysUserDomain domain = new SysUserDomain();
        domain.setAccount(new Account(new Username("user"), new Password("pass"), null, null, true));

        domain.updateUserStatus(false);

        assertFalse(domain.getAccount().lockStatus());
    }

    @Test
    void shouldCloneWithNullFields() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(null);
        domain.setAccount(null);
        domain.setExtendInfo(null);

        SysUserDomain clone = domain.clone();

        assertNotNull(clone);
        assertNull(clone.getUserId());
        assertNull(clone.getAccount());
        assertNull(clone.getExtendInfo());
    }

    @Test
    void shouldCloneWithAllFields() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        domain.setAccount(new Account(new Username("user"), new Password("pass"), "test@example.com", "12345678901", false));
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);
        domain.setExtendInfo(extendInfo);

        SysUserDomain clone = domain.clone();

        assertNotNull(clone);
        assertEquals(1L, clone.getUserId().value());
        assertEquals("user", clone.getAccount().username().value());
        assertEquals("avatar.png", clone.getExtendInfo().getAvatar());
        assertEquals(true, clone.getExtendInfo().getSex());
    }

    @Test
    void shouldHandleCloneNotSupportedException() {
        SysUserDomain domain = new SysUserDomain() {
            @Override
            protected Object doClone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException("test");
            }
        };
        assertThrows(AssertionError.class, domain::clone);
    }
}
