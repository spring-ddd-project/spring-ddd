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

                        String json = objectMapper.writeValueAsString(treeViewList);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                            ZipEntry entry = new ZipEntry("ddd_files.json");
                            zos.putNextEntry(entry);
                            zos.write(json.getBytes(StandardCharsets.UTF_8));
                            zos.closeEntry();
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
                        log.error("\n===> #GenDownloadDomainServiceImpl.download#:{}", e.toString());
                        return Mono.error(new RuntimeException("Error preparing ZIP file"));
                    }
                });
    }
}
