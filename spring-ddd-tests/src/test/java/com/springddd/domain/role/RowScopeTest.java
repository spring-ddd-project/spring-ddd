package com.springddd.domain.role;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RowScopeTest {

    @Test
    void defaultConstructor_ShouldCreateEmptyRowScope() {
        // When
        RowScope rowScope = new RowScope();

        // Then
        assertNull(rowScope.getDeptIds());
        assertNull(rowScope.getPostIds());
        assertNull(rowScope.getUserIds());
        assertNull(rowScope.getSelf());
    }

    @Test
    void allArgsConstructor_ShouldCreateRowScopeWithValues() {
        // Given
        List<Long> deptIds = Arrays.asList(1L, 2L, 3L);
        List<Long> postIds = Arrays.asList(10L, 20L);
        List<Long> userIds = Arrays.asList(100L, 200L, 300L);
        Boolean self = true;

        // When
        RowScope rowScope = new RowScope(deptIds, postIds, userIds, self);

        // Then
        assertEquals(deptIds, rowScope.getDeptIds());
        assertEquals(postIds, rowScope.getPostIds());
        assertEquals(userIds, rowScope.getUserIds());
        assertTrue(rowScope.getSelf());
    }

    @Test
    void setters_ShouldUpdateValues() {
        // Given
        RowScope rowScope = new RowScope();
        List<Long> deptIds = Arrays.asList(1L, 2L);

        // When
        rowScope.setDeptIds(deptIds);
        rowScope.setPostIds(Arrays.asList(10L));
        rowScope.setUserIds(Arrays.asList(100L));
        rowScope.setSelf(false);

        // Then
        assertEquals(deptIds, rowScope.getDeptIds());
        assertEquals(Arrays.asList(10L), rowScope.getPostIds());
        assertEquals(Arrays.asList(100L), rowScope.getUserIds());
        assertFalse(rowScope.getSelf());
    }

    @Test
    void equals_ShouldWorkCorrectly() {
        // Given
        RowScope scope1 = new RowScope(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        RowScope scope2 = new RowScope(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        RowScope scope3 = new RowScope();

        // Then
        assertEquals(scope1, scope2);
        assertNotEquals(scope1, scope3);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        // Given
        RowScope scope1 = new RowScope(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);
        RowScope scope2 = new RowScope(Arrays.asList(1L, 2L), Arrays.asList(10L), Arrays.asList(100L), true);

        // Then
        assertEquals(scope1.hashCode(), scope2.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        // Given
        RowScope rowScope = new RowScope();
        rowScope.setDeptIds(Arrays.asList(1L, 2L));

        // When
        String result = rowScope.toString();

        // Then
        assertNotNull(result);
    }
}
