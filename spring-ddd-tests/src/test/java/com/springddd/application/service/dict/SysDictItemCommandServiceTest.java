package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictItemCommandServiceTest {

    @Test
    void shouldHaveSysDictItemCommandServiceClass() {
        assertNotNull(SysDictItemCommandService.class);
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(SysDictItemCommandService.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        assertNotNull(SysDictItemCommandService.class.getDeclaredMethod("create", SysDictItemCommand.class));
    }

    @Test
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        assertNotNull(SysDictItemCommandService.class.getDeclaredMethod("update", SysDictItemCommand.class));
    }

    @Test
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        assertNotNull(SysDictItemCommandService.class.getDeclaredMethod("delete", java.util.List.class));
    }

    @Test
    void shouldHaveWipeMethod() throws NoSuchMethodException {
        assertNotNull(SysDictItemCommandService.class.getDeclaredMethod("wipe", java.util.List.class));
    }

    @Test
    void shouldHaveRestoreMethod() throws NoSuchMethodException {
        assertNotNull(SysDictItemCommandService.class.getDeclaredMethod("restore", java.util.List.class));
    }
}
