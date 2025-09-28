package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RowScopeTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        RowScope rowScope = new RowScope();
        assertNotNull(rowScope);
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        List<Long> deptIds = Arrays.asList(1L, 2L);
        List<Long> postIds = Arrays.asList(10L, 20L);
        List<Long> userIds = Arrays.asList(100L, 200L);
        Boolean self = true;

        RowScope rowScope = new RowScope(deptIds, postIds, userIds, self);

        assertEquals(deptIds, rowScope.getDeptIds());
        assertEquals(postIds, rowScope.getPostIds());
        assertEquals(userIds, rowScope.getUserIds());
        assertTrue(rowScope.getSelf());
    }

    @Test
    void shouldSetAndGetDeptIds() {
        RowScope rowScope = new RowScope();
        List<Long> deptIds = Arrays.asList(1L, 2L, 3L);

        rowScope.setDeptIds(deptIds);

        assertEquals(deptIds, rowScope.getDeptIds());
    }

    @Test
    void shouldSetAndGetPostIds() {
        RowScope rowScope = new RowScope();
        List<Long> postIds = Arrays.asList(10L, 20L);

        rowScope.setPostIds(postIds);

        assertEquals(postIds, rowScope.getPostIds());
    }

    @Test
    void shouldSetAndGetUserIds() {
        RowScope rowScope = new RowScope();
        List<Long> userIds = Arrays.asList(100L, 200L);

        rowScope.setUserIds(userIds);

        assertEquals(userIds, rowScope.getUserIds());
    }

    @Test
    void shouldSetAndGetSelf() {
        RowScope rowScope = new RowScope();

        rowScope.setSelf(true);
        assertTrue(rowScope.getSelf());

        rowScope.setSelf(false);
        assertFalse(rowScope.getSelf());
    }

    @Test
    void shouldHandleNullLists() {
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(null);
        rowScope.setPostIds(null);
        rowScope.setUserIds(null);

        assertNull(rowScope.getDeptIds());
        assertNull(rowScope.getPostIds());
        assertNull(rowScope.getUserIds());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RowScope rs1 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), true);
        RowScope rs2 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), true);
        assertEquals(rs1, rs2);
    }

    @Test
    void equals_shouldFailForDifferentDeptIds() {
        RowScope rs1 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), true);
        RowScope rs2 = new RowScope(Arrays.asList(9L), Arrays.asList(2L), Arrays.asList(3L), true);
        assertNotEquals(rs1, rs2);
    }

    @Test
    void equals_shouldFailForDifferentSelf() {
        RowScope rs1 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), true);
        RowScope rs2 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), false);
        assertNotEquals(rs1, rs2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RowScope rs1 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), true);
        RowScope rs2 = new RowScope(Arrays.asList(1L), Arrays.asList(2L), Arrays.asList(3L), true);
        assertEquals(rs1.hashCode(), rs2.hashCode());
    }

    @Test
    void toString_shouldReturnValues() {
        RowScope rowScope = new RowScope(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        String str = rowScope.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("100"));
    }
}
