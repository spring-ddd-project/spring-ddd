package com.springddd.application.service.menu;

import com.springddd.domain.menu.WipeSysMenuByIdsDomainService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WipeSysMenuByIdsDomainServiceImplTest {

    @Test
    void shouldHaveWipeSysMenuByIdsDomainServiceImplClass() {
        assertNotNull(WipeSysMenuByIdsDomainServiceImpl.class);
    }

    @Test
    void shouldImplementWipeSysMenuByIdsDomainService() {
        assertTrue(WipeSysMenuByIdsDomainService.class.isAssignableFrom(WipeSysMenuByIdsDomainServiceImpl.class));
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(WipeSysMenuByIdsDomainServiceImpl.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveDeleteByIdsMethod() {
        assertNotNull(WipeSysMenuByIdsDomainServiceImpl.class.getDeclaredMethod("deleteByIds", java.util.List.class));
    }
}
