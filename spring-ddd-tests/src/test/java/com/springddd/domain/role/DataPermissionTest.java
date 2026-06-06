package com.springddd.domain.role;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataPermissionTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        DataPermission dataPermission = new DataPermission();
        assertNotNull(dataPermission);
    }

    @Test
    void shouldCreateWithAllArgsConstructor() {
        RowScope rowScope = new RowScope();
        List<Long> deptIds = Arrays.asList(1L);

        DataPermission dataPermission = new DataPermission(rowScope, 1, deptIds);

        assertEquals(rowScope, dataPermission.getRowScope());
        assertEquals(1, dataPermission.getDataScope());
        assertEquals(deptIds, dataPermission.getDeptIds());
    }

    @Test
    void shouldSetAndGetRowScope() {
        DataPermission dataPermission = new DataPermission();
        RowScope rowScope = new RowScope();
        rowScope.setSelf(true);

        dataPermission.setRowScope(rowScope);

        assertEquals(rowScope, dataPermission.getRowScope());
        assertTrue(dataPermission.getRowScope().getSelf());
    }

    @Test
    void shouldHandleNullRowScope() {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setRowScope(null);
        assertNull(dataPermission.getRowScope());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        RowScope rowScope = new RowScope();
        DataPermission dp1 = new DataPermission(rowScope, 1, Arrays.asList(1L));
        DataPermission dp2 = new DataPermission(rowScope, 1, Arrays.asList(1L));
        assertEquals(dp1, dp2);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        RowScope rowScope = new RowScope();
        DataPermission dp1 = new DataPermission(rowScope, 1, Arrays.asList(1L));
        DataPermission dp2 = new DataPermission(rowScope, 1, Arrays.asList(1L));
        assertEquals(dp1.hashCode(), dp2.hashCode());
    }
}
