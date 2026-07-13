package com.springddd.application.service.common;

import com.springddd.domain.role.DataScope;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DataScopeResultTest {

    @Test
    void allResultShouldRepresentFullAccess() {
        DataScopeResult all = DataScopeResult.all();

        assertEquals(DataScope.ALL, all.getScope());
        assertTrue(all.getVisibleUsernames().isEmpty());
        assertTrue(all.isAll());
    }

    @Test
    void usernameSetResultShouldDefaultToPersonalScope() {
        Set<String> usernames = Set.of("zhangsan", "lisi");
        DataScopeResult result = new DataScopeResult(usernames);

        assertEquals(DataScope.PERSONAL, result.getScope());
        assertEquals(usernames, result.getVisibleUsernames());
        assertFalse(result.isAll());
    }

    @Test
    void explicitConstructorShouldPreserveScopeAndUsernames() {
        Set<String> usernames = Set.of("zhangsan");
        DataScopeResult result = new DataScopeResult(DataScope.DEPT_ONLY, usernames);

        assertEquals(DataScope.DEPT_ONLY, result.getScope());
        assertEquals(usernames, result.getVisibleUsernames());
        assertFalse(result.isAll());
    }
}
