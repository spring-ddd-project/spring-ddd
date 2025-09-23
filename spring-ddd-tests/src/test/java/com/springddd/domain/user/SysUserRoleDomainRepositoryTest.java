package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.AggregateRootId;
import com.springddd.domain.DomainRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SysUserRoleDomainRepository interface.
 * Verifies the interface contract and method signatures.
 */
class SysUserRoleDomainRepositoryTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(SysUserRoleDomainRepository.class.isInterface());
    }

    @Test
    void interface_ShouldExtendDomainRepository() {
        assertTrue(DomainRepository.class.isAssignableFrom(SysUserRoleDomainRepository.class));
    }

    @Test
    void shouldBeAssignableFromDomainRepository() {
        // Verify SysUserRoleDomainRepository can be assigned to DomainRepository type
        // by checking the class assignment works
        Class<?> repoInterface = SysUserRoleDomainRepository.class;
        assertTrue(DomainRepository.class.isAssignableFrom(repoInterface));
    }

    @Test
    void shouldHaveCorrectTypeParameters() {
        Type[] interfaces = SysUserRoleDomainRepository.class.getGenericInterfaces();
        assertEquals(1, interfaces.length);

        ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        assertEquals(2, typeArguments.length);
        assertEquals(UserRoleId.class, typeArguments[0]);
        assertEquals(SysUserRoleDomain.class, typeArguments[1]);
    }

    @Test
    void parentInterface_ShouldHaveLoadMethod() throws NoSuchMethodException {
        // Verify the parent DomainRepository interface has the load method
        Method loadMethod = DomainRepository.class.getMethod("load", AggregateRootId.class);
        assertNotNull(loadMethod);
        assertEquals(reactor.core.publisher.Mono.class, loadMethod.getReturnType());
    }

    @Test
    void parentInterface_ShouldHaveSaveMethod() throws NoSuchMethodException {
        // Verify the parent DomainRepository interface has the save method
        Method saveMethod = DomainRepository.class.getMethod("save", AbstractDomainMask.class);
        assertNotNull(saveMethod);
        assertEquals(reactor.core.publisher.Mono.class, saveMethod.getReturnType());
    }
}
