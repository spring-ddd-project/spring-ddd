package com.springddd.domain.role;

import com.springddd.domain.AbstractDomainMask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysRoleDomainTest {

    @Test
    void shouldHaveSysRoleDomainClass() {
        assertNotNull(SysRoleDomain.class);
    }

    @Test
    void shouldExtendAbstractDomainMask() {
        assertTrue(AbstractDomainMask.class.isAssignableFrom(SysRoleDomain.class));
    }

    @Test
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        assertNotNull(SysRoleDomain.class.getDeclaredMethod("create"));
    }

    @Test
    void shouldHaveUpdateRoleMethod() throws NoSuchMethodException {
        assertNotNull(SysRoleDomain.class.getDeclaredMethod("updateRole", RoleBasicInfo.class, RoleExtendInfo.class, DataPermission.class, Long.class));
    }

    @Test
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        assertNotNull(SysRoleDomain.class.getDeclaredMethod("delete"));
    }

    @Test
    void shouldHaveRestoreMethod() throws NoSuchMethodException {
        assertNotNull(SysRoleDomain.class.getDeclaredMethod("restore"));
    }

    @Test
    void create_shouldNotThrowException() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.create();
        assertNotNull(domain);
    }

    @Test
    void delete_shouldSetDeleteStatusToTrue() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.delete();
        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldSetDeleteStatusToFalse() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertFalse(domain.getDeleteStatus());
    }
}
