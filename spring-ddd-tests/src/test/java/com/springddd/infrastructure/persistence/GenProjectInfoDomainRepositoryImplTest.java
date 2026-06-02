package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenProjectInfoDomainRepositoryImplTest {

    @Mock
    private GenProjectInfoRepository genProjectInfoRepository;

    @InjectMocks
    private GenProjectInfoDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setTableName("sys_user");
        entity.setPackageName("com.springddd");
        entity.setClassName("SysUser");
        entity.setModuleName("system");
        entity.setProjectName("spring-ddd");
        entity.setRequestName("SysUserRequest");
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(genProjectInfoRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new InfoId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getId().value());
                    assertEquals("sys_user", domain.getProjectInfo().tableName());
                    assertEquals("SysUserRequest", domain.getExtendInfo().requestName());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(genProjectInfoRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new InfoId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(null);
        domain.setProjectInfo(new ProjectInfo("sys_user", "com.springddd", "SysUser", "system", "spring-ddd"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("SysUserRequest"));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        GenProjectInfoEntity savedEntity = new GenProjectInfoEntity();
        savedEntity.setId(1L);

        when(genProjectInfoRepository.save(any(GenProjectInfoEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(1L));
        domain.setProjectInfo(new ProjectInfo("sys_user", "com.springddd", "SysUser", "system", "spring-ddd"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("SysUserRequest"));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenProjectInfoEntity savedEntity = new GenProjectInfoEntity();
        savedEntity.setId(1L);

        when(genProjectInfoRepository.save(any(GenProjectInfoEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
