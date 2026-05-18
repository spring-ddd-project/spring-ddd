package com.springddd.web;

import com.springddd.application.service.permission.ColumnPermissionMetadataService;
import com.springddd.domain.permission.EntityColumnMetadata;
import com.springddd.domain.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/sys/column-permission")
@RequiredArgsConstructor
public class ColumnPermissionMetadataController {

    private final ColumnPermissionMetadataService metadataService;

    @PostMapping("/metadata")
    public Mono<ApiResponse> getMetadata() {
        return ApiResponse.ok(metadataService.getCurrentUserColumnPermissions());
    }

    @PostMapping("/visible-columns")
    public Mono<ApiResponse> getVisibleColumns(@RequestParam String entityCode) {
        return ApiResponse.ok(metadataService.getVisibleColumns(entityCode));
    }

    @PostMapping("/entities")
    public Mono<ApiResponse> getAllEntities() {
        return ApiResponse.ok(metadataService.getAllEntityMetadata());
    }
}
