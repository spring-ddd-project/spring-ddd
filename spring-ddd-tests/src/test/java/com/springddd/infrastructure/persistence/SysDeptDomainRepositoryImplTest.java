package com.springddd.infrastructure.persistence;

import com.springddd.domain.dept.*;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysDeptDomainRepositoryImpl Tests")
class SysDeptDomainRepositoryImplTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @InjectMocks
    private SysDeptDomainRepositoryImpl sysDeptDomainRepository;

    private SysDeptEntity testEntity;
    private SysDeptDomain testDomain;

    @BeforeEach
    void setUp() {
        testEntity = new SysDeptEntity();
        testEntity.setId(1L);
        testEntity.setParentId(0L);
        testEntity.setDeptName("Test Department");
        testEntity.setSortOrder(1);
        testEntity.setDeptStatus(true);
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
        when(sysDeptRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysDeptDomainRepository.load(new DeptId(1L)))
                .assertNext(domain -> {
                    org.assertj.core.api.Assertions.assertThat(domain.getId().value()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(domain.getParentId().value()).isEqualTo(0L);
                    org.assertj.core.api.Assertions.assertThat(domain.getDeptBasicInfo().deptName()).isEqualTo("Test Department");
                    org.assertj.core.api.Assertions.assertThat(domain.getDeptExtendInfo().sortOrder()).isEqualTo(1);
                    org.assertj.core.api.Assertions.assertThat(domain.getDeptExtendInfo().deptStatus()).isTrue();
                    org.assertj.core.api.Assertions.assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysDeptRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysDeptDomainRepository.load(new DeptId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysDeptRepository.save(any(SysDeptEntity.class))).thenReturn(Mono.just(testEntity));

        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(1L));
        domain.setParentId(new DeptId(0L));
        DeptBasicInfo basicInfo = new DeptBasicInfo("Test Department");
        domain.setDeptBasicInfo(basicInfo);
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);
        domain.setDeptExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysDeptDomainRepository.save(domain))
                .assertNext(id -> org.assertj.core.api.Assertions.assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysDeptEntity newEntity = new SysDeptEntity();
        newEntity.setId(2L);
        newEntity.setParentId(0L);
        newEntity.setDeptName("New Department");

        when(sysDeptRepository.save(any(SysDeptEntity.class))).thenReturn(Mono.just(newEntity));

        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(null);
        domain.setParentId(new DeptId(0L));
        DeptBasicInfo basicInfo = new DeptBasicInfo("New Department");
        domain.setDeptBasicInfo(basicInfo);
        DeptExtendInfo extendInfo = new DeptExtendInfo(1, true);
        domain.setDeptExtendInfo(extendInfo);

        StepVerifier.create(sysDeptDomainRepository.save(domain))
                .assertNext(id -> org.assertj.core.api.Assertions.assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
