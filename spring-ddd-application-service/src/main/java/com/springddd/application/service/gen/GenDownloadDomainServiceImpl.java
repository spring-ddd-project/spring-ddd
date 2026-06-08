package com.springddd.application.service.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.gen.GenDownloadDomainService;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenDownloadDomainServiceImpl implements GenDownloadDomainService {

    private final ReactiveRedisCacheHelper cacheHelper;

    private final ObjectMapper objectMapper;

    @Override
    public Mono<ResponseEntity<Resource>> download() {
        return cacheHelper.getCache(
                        CacheKeys.GEN_FILES.buildKey(SecurityUtils.getUserId()),
                        List.class
                )
                .flatMap(list -> {
                    try {
                        List<ProjectTreeView> treeViewList = objectMapper.convertValue(list, new TypeReference<>() {
                        });

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                            createZipFromTree(treeViewList, zos, "");
                            zos.finish();
                        }

                        byte[] zipBytes = baos.toByteArray();
                        ByteArrayResource resource = new ByteArrayResource(zipBytes);

                        return Mono.just(
                                ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ddd_files.zip")
                                        .contentLength(zipBytes.length)
                                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                        .body(resource)
                        );

                    } catch (Exception e) {
                        log.error("Error preparing ZIP file: {}", e.getMessage(), e);
                        return Mono.error(new RuntimeException("Error preparing ZIP file"));
                    }
                });
    }

    private void createZipFromTree(List<ProjectTreeView> treeList, ZipOutputStream zos, String parentPath) throws IOException {
        for (ProjectTreeView node : treeList) {
            String currentPath = parentPath.isEmpty() ? node.getLabel() : parentPath + "/" + node.getLabel();

            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                zos.putNextEntry(new ZipEntry(currentPath + "/"));
                zos.closeEntry();

                createZipFromTree(node.getChildren(), zos, currentPath);
            } else {
                zos.putNextEntry(new ZipEntry(currentPath));
                if (node.getValue() != null) {
                    zos.write(node.getValue().getBytes(StandardCharsets.UTF_8));
                }
                zos.closeEntry();
            }
        }
    }
}
