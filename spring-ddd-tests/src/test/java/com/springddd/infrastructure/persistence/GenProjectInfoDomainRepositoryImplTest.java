package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenProjectInfoDomainRepositoryImpl Tests")
class GenProjectInfoDomainRepositoryImplTest {

    @Mock
    private GenProjectInfoRepository genProjectInfoRepository;

    @InjectMocks
    private GenProjectInfoDomainRepositoryImpl genProjectInfoDomainRepository;

    private GenProjectInfoEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new GenProjectInfoEntity();
        testEntity.setId(1L);
        testEntity.setTableName("sys_user");
        testEntity.setPackageName("com.example.user");
        testEntity.setClassName("UserController");
        testEntity.setModuleName("user");
        testEntity.setProjectName("example-project");
        testEntity.setRequestName("UserRequest");
        testEntity.setDeleteStatus(false);
        testEntity.setCreateBy("admin");
        testEntity.setCreateTime(LocalDateTime.now());
        testEntity.setUpdateBy("admin");
        testEntity.setUpdateTime(LocalDateTime.now());
        testEntity.setVersion(1);
    }

    @Test
    @DisplayName("load() should return domain when entity exists")
    void load_WhenEntityExists_ReturnsDomain() {
        when(genProjectInfoRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(genProjectInfoDomainRepository.load(new InfoId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getId().value()).isEqualTo(1L);
                    assertThat(domain.getProjectInfo().tableName()).isEqualTo("sys_user");
                    assertThat(domain.getProjectInfo().packageName()).isEqualTo("com.example.user");
                    assertThat(domain.getProjectInfo().className()).isEqualTo("UserController");
                    assertThat(domain.getProjectInfo().moduleName()).isEqualTo("user");
                    assertThat(domain.getProjectInfo().projectName()).isEqualTo("example-project");
                    assertThat(domain.getExtendInfo().requestName()).isEqualTo("UserRequest");
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(genProjectInfoRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(genProjectInfoDomainRepository.load(new InfoId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(genProjectInfoRepository.save(any(GenProjectInfoEntity.class))).thenReturn(Mono.just(testEntity));

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(1L));
        domain.setProjectInfo(new ProjectInfo("sys_user", "com.example.user", "UserController", "user", "example-project"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("UserRequest"));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(genProjectInfoDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        GenProjectInfoEntity newEntity = new GenProjectInfoEntity();
        newEntity.setId(2L);
        when(genProjectInfoRepository.save(any(GenProjectInfoEntity.class))).thenReturn(Mono.just(newEntity));

        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(null);
        domain.setProjectInfo(new ProjectInfo("sys_role", "com.example.role", "RoleController", "role", "example-project"));
        domain.setExtendInfo(new GenProjectInfoExtendInfo("RoleRequest"));

        StepVerifier.create(genProjectInfoDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
