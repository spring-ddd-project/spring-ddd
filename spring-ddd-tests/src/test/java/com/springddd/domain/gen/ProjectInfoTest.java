package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ClassNameNullException;
import com.springddd.domain.gen.exception.ModuleNameNullException;
import com.springddd.domain.gen.exception.PackageNameNullException;
import com.springddd.domain.gen.exception.ProjectNameNullException;
import com.springddd.domain.gen.exception.TableNameNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectInfoTest {

    @Test
    @DisplayName("正常构造")
    void constructor_withValidValue_shouldCreate() {
        ProjectInfo obj = new ProjectInfo("test", "test", "test", "test", "test");
        assertThat(obj.tableName()).isEqualTo("test");
        assertThat(obj.packageName()).isEqualTo("test");
        assertThat(obj.className()).isEqualTo("test");
        assertThat(obj.moduleName()).isEqualTo("test");
        assertThat(obj.projectName()).isEqualTo("test");
    }

    @Test
    @DisplayName("tableName 为 null 应抛异常")
    void constructor_withNullTablename_shouldThrowException() {
        assertThatThrownBy(() -> new ProjectInfo(null, "test", "test", "test", "test"))
                .isInstanceOf(TableNameNullException.class);
    }

    @Test
    @DisplayName("packageName 为 null 应抛异常")
    void constructor_withNullPackagename_shouldThrowException() {
        assertThatThrownBy(() -> new ProjectInfo("test", null, "test", "test", "test"))
                .isInstanceOf(PackageNameNullException.class);
    }

    @Test
    @DisplayName("className 为 null 应抛异常")
    void constructor_withNullClassname_shouldThrowException() {
        assertThatThrownBy(() -> new ProjectInfo("test", "test", null, "test", "test"))
                .isInstanceOf(ClassNameNullException.class);
    }

    @Test
    @DisplayName("moduleName 为 null 应抛异常")
    void constructor_withNullModulename_shouldThrowException() {
        assertThatThrownBy(() -> new ProjectInfo("test", "test", "test", null, "test"))
                .isInstanceOf(ModuleNameNullException.class);
    }

    @Test
    @DisplayName("projectName 为 null 应抛异常")
    void constructor_withNullProjectname_shouldThrowException() {
        assertThatThrownBy(() -> new ProjectInfo("test", "test", "test", "test", null))
                .isInstanceOf(ProjectNameNullException.class);
    }

}