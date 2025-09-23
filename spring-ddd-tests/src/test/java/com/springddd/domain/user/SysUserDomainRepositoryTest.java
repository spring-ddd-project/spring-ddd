package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.AggregateRootId;
import com.springddd.domain.DomainRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SysUserDomainRepository interface.
 * Verifies the interface contract and method signatures.
 */
class SysUserDomainRepositoryTest {

    @Test
    void interface_ShouldExist() {
        assertTrue(SysUserDomainRepository.class.isInterface());
    }

    @Test
    void interface_ShouldExtendDomainRepository() {
        assertTrue(DomainRepository.class.isAssignableFrom(SysUserDomainRepository.class));
    }

    @Test
    void shouldBeAssignableFromDomainRepository() {
        // Verify SysUserDomainRepository can be assigned to DomainRepository type
        // by checking the class assignment works
        Class<?> repoInterface = SysUserDomainRepository.class;
        assertTrue(DomainRepository.class.isAssignableFrom(repoInterface));
    }

    @Test
    void shouldHaveCorrectTypeParameters() {
        Type[] interfaces = SysUserDomainRepository.class.getGenericInterfaces();
        assertEquals(1, interfaces.length);

        ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        assertEquals(2, typeArguments.length);
        assertEquals(UserId.class, typeArguments[0]);
        assertEquals(SysUserDomain.class, typeArguments[1]);
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
