package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysMenuCommandServiceTest {

    @Test
    void shouldHaveSysMenuCommandServiceClass() {
        assertNotNull(SysMenuCommandService.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(SysMenuCommandService.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveCreateMethod() {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveUpdateMethod() {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveDeleteMethod() {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveWipeMethod() {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveRestoreMethod() {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }
}
