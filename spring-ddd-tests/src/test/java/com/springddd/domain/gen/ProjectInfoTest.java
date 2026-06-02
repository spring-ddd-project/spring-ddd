package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectInfoTest {

    @Test
    void shouldCreateProjectInfoWithValidValues() {
        ProjectInfo info = new ProjectInfo("table_a", "com.example", "ClassA", "module_a", "project_a");
        assertEquals("table_a", info.tableName());
        assertEquals("com.example", info.packageName());
        assertEquals("ClassA", info.className());
        assertEquals("module_a", info.moduleName());
        assertEquals("project_a", info.projectName());
    }

    @Test
    void equals_shouldWorkForSameValues() {
        ProjectInfo info1 = new ProjectInfo("table_a", "com.example", "ClassA", "module_a", "project_a");
        ProjectInfo info2 = new ProjectInfo("table_a", "com.example", "ClassA", "module_a", "project_a");
        assertEquals(info1, info2);
    }

    @Test
    void toString_shouldReturnValueAsString() {
        ProjectInfo info = new ProjectInfo("table_a", "com.example", "ClassA", "module_a", "project_a");
        String str = info.toString();
        assertTrue(str.contains("ProjectInfo"));
    }

    @Test
    void shouldThrowWhenTableNameIsNull() {
        assertThrows(TableNameNullException.class, () ->
            new ProjectInfo(null, "com.example", "ClassA", "module_a", "project_a"));
    }

    @Test
    void shouldThrowWhenPackageNameIsNull() {
        assertThrows(PackageNameNullException.class, () ->
            new ProjectInfo("table_a", null, "ClassA", "module_a", "project_a"));
    }

    @Test
    void shouldThrowWhenClassNameIsNull() {
        assertThrows(ClassNameNullException.class, () ->
            new ProjectInfo("table_a", "com.example", null, "module_a", "project_a"));
    }

    @Test
    void shouldThrowWhenModuleNameIsNull() {
        assertThrows(ModuleNameNullException.class, () ->
            new ProjectInfo("table_a", "com.example", "ClassA", null, "project_a"));
    }

    @Test
    void shouldThrowWhenProjectNameIsNull() {
        assertThrows(ProjectNameNullException.class, () ->
            new ProjectInfo("table_a", "com.example", "ClassA", "module_a", null));
    }
}
