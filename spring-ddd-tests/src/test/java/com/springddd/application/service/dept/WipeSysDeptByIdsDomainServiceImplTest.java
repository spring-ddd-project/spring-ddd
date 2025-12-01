package com.springddd.application.service.dept;

import com.springddd.domain.dept.WipeSysDeptByIdsDomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WipeSysDeptByIdsDomainServiceImplTest {

    @Test
    void shouldHaveWipeSysDeptByIdsDomainServiceImplClass() {
        assertNotNull(WipeSysDeptByIdsDomainServiceImpl.class);
    }

    @Test
    void shouldImplementWipeSysDeptByIdsDomainService() {
        assertTrue(WipeSysDeptByIdsDomainService.class.isAssignableFrom(WipeSysDeptByIdsDomainServiceImpl.class));
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(WipeSysDeptByIdsDomainServiceImpl.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveDeleteByIdsMethod() {
        assertNotNull(WipeSysDeptByIdsDomainServiceImpl.class.getDeclaredMethod("deleteByIds", java.util.List.class));
    }
}
