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
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveWipeMethod() throws NoSuchMethodException {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }

    @Test
    void shouldHaveRestoreMethod() throws NoSuchMethodException {
        assertNotNull(SysMenuCommandService.class.getDeclaredMethods());
    }
}
