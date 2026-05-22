package com.springddd.application.service.permission;

import com.springddd.domain.permission.EntityColumnMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ColumnPermissionMetadataProvider {

    private final EntityMetadataScanner entityMetadataScanner;

    public List<EntityColumnMetadata> getAllMetadata() {
        return entityMetadataScanner.getAllMetadata();
    }
}
