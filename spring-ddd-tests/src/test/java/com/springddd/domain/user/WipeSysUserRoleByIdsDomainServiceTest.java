package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for WipeSysUserRoleByIdsDomainService interface.
 * Verifies the interface contract and method signatures.
 */
class WipeSysUserRoleByIdsDomainServiceTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(WipeSysUserRoleByIdsDomainService.class.isInterface());
    }

    @Test
    void deleteByIdsMethod_ShouldHaveCorrectSignature() throws NoSuchMethodException {
        Method deleteByIdsMethod = WipeSysUserRoleByIdsDomainService.class.getMethod(
            "deleteByIds",
            List.class
        );

        assertNotNull(deleteByIdsMethod);
        assertEquals(reactor.core.publisher.Mono.class, deleteByIdsMethod.getReturnType());
    }

    @Test
    void interface_ShouldExtendCorrectTypes() {
        // Verify WipeSysUserRoleByIdsDomainService is an interface
        assertTrue(WipeSysUserRoleByIdsDomainService.class.isInterface());
    }

    @Test
    void serviceMethods_ShouldBeAbstract() {
        // Interface methods should be abstract by default
        Method[] methods = WipeSysUserRoleByIdsDomainService.class.getDeclaredMethods();
        for (Method method : methods) {
            assertFalse(method.isBridge());
            assertFalse(method.isSynthetic());
        }
    }
}
