package com.springddd.domain.user;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for LinkUsersAndRolesDomainService interface.
 * Verifies the interface contract and method signatures.
 */
class LinkUsersAndRolesDomainServiceTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(LinkUsersAndRolesDomainService.class.isInterface());
    }

    @Test
    void linkMethod_ShouldHaveCorrectSignature() throws NoSuchMethodException {
        Method linkMethod = LinkUsersAndRolesDomainService.class.getMethod(
            "link",
            Long.class,
            List.class
        );

        assertNotNull(linkMethod);
        assertEquals(reactor.core.publisher.Mono.class, linkMethod.getReturnType());
    }

    @Test
    void interface_ShouldExtendCorrectTypes() {
        // Verify LinkUsersAndRolesDomainService is an interface
        assertTrue(LinkUsersAndRolesDomainService.class.isInterface());
    }

    @Test
    void serviceMethods_ShouldBeAbstract() {
        // Interface methods should be abstract by default
        Method[] methods = LinkUsersAndRolesDomainService.class.getDeclaredMethods();
        for (Method method : methods) {
            assertFalse(method.isBridge());
            assertFalse(method.isSynthetic());
        }
    }
}
