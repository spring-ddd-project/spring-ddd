package com.springddd.application.service.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenDownloadDomainServiceImplTest {

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GenDownloadDomainServiceImpl downloadDomainService;

    private MockedStatic<ReactiveSecurityUtils> securityUtilsMock;

    @AfterEach
    void tearDown() {
        if (securityUtilsMock != null) {
            securityUtilsMock.close();
        }
    }

    @Test
    void download_shouldUseTableNameAsZipFileName() {
        Long userId = 1L;
        String tableName = "test_user";
        List<ProjectTreeView> treeList = new ArrayList<>();

        securityUtilsMock = mockStatic(ReactiveSecurityUtils.class);
        securityUtilsMock.when(ReactiveSecurityUtils::getCurrentUserId).thenReturn(Mono.just(userId));

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(userId), List.class))
                .thenReturn(Mono.just(treeList));
        when(cacheHelper.getCache(CacheKeys.GEN_TABLE_NAME.buildKey(userId), String.class))
                .thenReturn(Mono.just(tableName));
        when(objectMapper.convertValue(eq(treeList), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(downloadDomainService.download())
                .assertNext(response -> {
                    ResponseEntity<Resource> entity = (ResponseEntity<Resource>) response;
                    String contentDisposition = entity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
                    String expectedFilename = URLEncoder.encode(tableName + ".zip", StandardCharsets.UTF_8)
                            .replace("+", "%20");
                    assertTrue(contentDisposition.contains("filename*=UTF-8''" + expectedFilename));
                    assertEquals(tableName + ".zip", decodeFilename(contentDisposition));
                })
                .verifyComplete();
    }

    @Test
    void download_shouldFallbackToDefaultName_whenTableNameNotCached() {
        Long userId = 1L;
        List<ProjectTreeView> treeList = new ArrayList<>();

        securityUtilsMock = mockStatic(ReactiveSecurityUtils.class);
        securityUtilsMock.when(ReactiveSecurityUtils::getCurrentUserId).thenReturn(Mono.just(userId));

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(userId), List.class))
                .thenReturn(Mono.just(treeList));
        when(cacheHelper.getCache(CacheKeys.GEN_TABLE_NAME.buildKey(userId), String.class))
                .thenReturn(Mono.empty());
        when(objectMapper.convertValue(eq(treeList), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(downloadDomainService.download())
                .assertNext(response -> {
                    ResponseEntity<Resource> entity = (ResponseEntity<Resource>) response;
                    String contentDisposition = entity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
                    String expectedFilename = URLEncoder.encode("ddd_files.zip", StandardCharsets.UTF_8)
                            .replace("+", "%20");
                    assertTrue(contentDisposition.contains("filename*=UTF-8''" + expectedFilename));
                })
                .verifyComplete();
    }

    private String decodeFilename(String contentDisposition) {
        String prefix = "filename*=UTF-8''";
        int start = contentDisposition.indexOf(prefix);
        if (start == -1) {
            return "";
        }
        String encoded = contentDisposition.substring(start + prefix.length());
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }
}
