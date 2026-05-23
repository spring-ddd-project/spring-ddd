package com.springddd.application.service.permission;

import com.springddd.domain.permission.EntityColumnMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColumnPermissionMetadataProviderTest {

    @Mock
    private EntityMetadataScanner entityMetadataScanner;

    @InjectMocks
    private ColumnPermissionMetadataProvider provider;

    @Test
    @DisplayName("getAllMetadata 应返回所有实体元数据")
    void getAllMetadata_shouldReturnAll() {
        EntityColumnMetadata metadata = new EntityColumnMetadata("sys_user", "用户", List.of(new EntityColumnMetadata.ColumnInfo("username", "用户名")));
        when(entityMetadataScanner.getAllMetadata()).thenReturn(List.of(metadata));

        List<EntityColumnMetadata> result = provider.getAllMetadata();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).entityCode()).isEqualTo("sys_user");
    }
}
