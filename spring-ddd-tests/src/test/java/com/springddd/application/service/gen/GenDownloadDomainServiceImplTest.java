package com.springddd.application.service.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.GenDownloadDomainServiceImpl;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenDownloadDomainServiceImplTest {

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GenDownloadDomainServiceImpl genDownloadDomainService;

    private List<ProjectTreeView> mockTreeViewList;

    @BeforeEach
    void setUp() {
        SecurityUtils.setUserId(1L);

        ProjectTreeView file1 = new ProjectTreeView();
        file1.setLabel("file1.txt");
        file1.setValue("content1");

        ProjectTreeView folder1 = new ProjectTreeView();
        folder1.setLabel("folder1");

        ProjectTreeView file2 = new ProjectTreeView();
        file2.setLabel("file2.txt");
        file2.setValue("content2");
        folder1.setChildren(Collections.singletonList(file2));

        mockTreeViewList = Arrays.asList(file1, folder1);
    }

    @Test
    void download_shouldReturnZipFile() throws Exception {
        when(cacheHelper.getCache(anyString(), eq(List.class))).thenReturn(Mono.just((List<?>) mockTreeViewList));
        when(objectMapper.convertValue(any(), any(com.fasterxml.jackson.core.type.TypeReference.class))).thenReturn(mockTreeViewList);

        StepVerifier.create(genDownloadDomainService.download())
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertTrue(response.getHeaders().getContentDisposition().toString().contains("attachment"));
                    assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
                    assertNotNull(response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void download_shouldHandleEmptyList() throws Exception {
        List<ProjectTreeView> emptyList = Collections.emptyList();

        when(cacheHelper.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(Collections.emptyList()));
        when(objectMapper.convertValue(any(), any(com.fasterxml.jackson.core.type.TypeReference.class))).thenReturn(emptyList);

        StepVerifier.create(genDownloadDomainService.download())
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void download_shouldReturnEmptyWhenCacheMiss() {
        when(cacheHelper.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(genDownloadDomainService.download())
                .verifyComplete();
    }
}
