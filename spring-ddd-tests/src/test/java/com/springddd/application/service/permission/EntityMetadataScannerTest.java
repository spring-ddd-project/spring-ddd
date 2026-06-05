package com.springddd.application.service.permission;

import com.springddd.domain.permission.EntityColumnMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EntityMetadataScannerTest {

    private EntityMetadataScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new EntityMetadataScanner();
        scanner.init();
    }

    @Test
    @DisplayName("getAllMetadata 应返回非空列表且包含已知实体")
    void getAllMetadata_shouldReturnNonEmptyListWithKnownEntities() {
        List<EntityColumnMetadata> metadata = scanner.getAllMetadata();

        assertThat(metadata).isNotEmpty();
        assertThat(metadata)
                .extracting(EntityColumnMetadata::entityCode)
                .contains("sys_user", "sys_dept");
    }

    @Test
    @DisplayName("getMetadata(sys_user) 应返回包含正确信息的 Optional")
    void getMetadata_forSysUser_shouldReturnCorrectMetadata() {
        Optional<EntityColumnMetadata> optional = scanner.getMetadata("sys_user");

        assertThat(optional).isPresent();
        EntityColumnMetadata metadata = optional.get();
        assertThat(metadata.entityCode()).isEqualTo("sys_user");
        assertThat(metadata.entityName()).isEqualTo("用户管理");
        assertThat(metadata.columns())
                .extracting(EntityColumnMetadata.ColumnInfo::field)
                .contains("id", "username", "deptId", "createBy");
    }

    @Test
    @DisplayName("hasEntity(sys_user) 应返回 true")
    void hasEntity_forExistingEntity_shouldReturnTrue() {
        assertThat(scanner.hasEntity("sys_user")).isTrue();
    }

    @Test
    @DisplayName("hasEntity(nonexistent) 应返回 false")
    void hasEntity_forNonExistentEntity_shouldReturnFalse() {
        assertThat(scanner.hasEntity("nonexistent")).isFalse();
    }

    @Test
    @DisplayName("hasField(sys_user, deptId) 应返回 true")
    void hasField_forExistingField_shouldReturnTrue() {
        assertThat(scanner.hasField("sys_user", "deptId")).isTrue();
    }

    @Test
    @DisplayName("hasField(sys_user, nonexistent) 应返回 false")
    void hasField_forNonExistentField_shouldReturnFalse() {
        assertThat(scanner.hasField("sys_user", "nonexistent")).isFalse();
    }

    @Test
    @DisplayName("getMetadata(test_no_dp) 应返回以表名作为实体名的元数据")
    void getMetadata_forEntityWithoutDataPermission_shouldUseTableNameAsEntityName() {
        Optional<EntityColumnMetadata> optional = scanner.getMetadata("test_no_dp");

        assertThat(optional).isPresent();
        EntityColumnMetadata metadata = optional.get();
        assertThat(metadata.entityCode()).isEqualTo("test_no_dp");
        assertThat(metadata.entityName()).isEqualTo("test_no_dp");
    }

    @Test
    @DisplayName("getMetadata(test_empty_name) 应返回以表名作为实体名的元数据")
    void getMetadata_forEntityWithEmptyDataPermissionName_shouldUseTableNameAsEntityName() {
        Optional<EntityColumnMetadata> optional = scanner.getMetadata("test_empty_name");

        assertThat(optional).isPresent();
        EntityColumnMetadata metadata = optional.get();
        assertThat(metadata.entityCode()).isEqualTo("test_empty_name");
        assertThat(metadata.entityName()).isEqualTo("test_empty_name");
    }

    @Test
    @DisplayName("getMetadata(test_static_field) 应跳过 static 字段和 serialVersionUID")
    void getMetadata_forEntityWithStaticFields_shouldSkipStaticFields() {
        Optional<EntityColumnMetadata> optional = scanner.getMetadata("test_static_field");

        assertThat(optional).isPresent();
        EntityColumnMetadata metadata = optional.get();
        assertThat(metadata.columns())
                .extracting(EntityColumnMetadata.ColumnInfo::field)
                .containsExactlyInAnyOrder("id", "name")
                .doesNotContain("serialVersionUID", "STATIC_FIELD");
    }

    @Test
    @DisplayName("hasField 当实体不存在时应返回 false")
    void hasField_forNonExistentEntity_shouldReturnFalse() {
        assertThat(scanner.hasField("nonexistent", "id")).isFalse();
    }

    @Test
    @DisplayName("extractColumns 应跳过非 static 的 serialVersionUID 字段")
    void extractColumns_shouldSkipNonStaticSerialVersionUID() throws Exception {
        Method method = EntityMetadataScanner.class.getDeclaredMethod("extractColumns", Class.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<EntityColumnMetadata.ColumnInfo> columns = (List<EntityColumnMetadata.ColumnInfo>) method.invoke(scanner,
                Class.forName("com.springddd.infrastructure.persistence.entity.TestSerialUidEntity"));

        assertThat(columns)
                .extracting(EntityColumnMetadata.ColumnInfo::field)
                .contains("id", "name")
                .doesNotContain("serialVersionUID");
    }

    @Test
    @DisplayName("extractColumns 对继承字段应去重")
    void extractColumns_withInheritance_shouldDeduplicateFields() throws Exception {
        Method method = EntityMetadataScanner.class.getDeclaredMethod("extractColumns", Class.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<EntityColumnMetadata.ColumnInfo> columns = (List<EntityColumnMetadata.ColumnInfo>) method.invoke(scanner,
                Class.forName("com.springddd.infrastructure.persistence.entity.TestChildEntity"));

        // id appears in both parent and child, but should only be listed once
        // Order: child fields first, then parent fields (duplicates skipped)
        assertThat(columns)
                .extracting(EntityColumnMetadata.ColumnInfo::field)
                .containsExactly("id", "childName", "name");
    }

    @Test
    @DisplayName("scanEntities 当 loadEntityClass 抛出 ClassNotFoundException 时应记录警告并跳过")
    void scanEntities_whenClassNotFound_shouldLogWarning() throws Exception {
        EntityMetadataScanner spyScanner = spy(new EntityMetadataScanner());
        doThrow(new ClassNotFoundException("test")).when(spyScanner).loadEntityClass(anyString());
        spyScanner.init();

        assertThat(spyScanner.getAllMetadata()).isEmpty();
    }
}
