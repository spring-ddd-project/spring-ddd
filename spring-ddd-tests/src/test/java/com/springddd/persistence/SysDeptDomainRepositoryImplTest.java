package com.springddd.persistence;

import com.springddd.domain.dept.*;
import com.springddd.infrastructure.persistence.SysDeptDomainRepositoryImpl;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
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
class SysDeptDomainRepositoryImplTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @InjectMocks
    private SysDeptDomainRepositoryImpl sysDeptDomainRepository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysDeptEntity entity = createTestEntity();

        when(sysDeptRepository.findById(1L)).thenReturn(Mono.just(entity));

        sysDeptDomainRepository.load(new DeptId(1L))
                .as(StepVerifier::create)
                .expectNextMatches(domain -> {
                    return domain.getId().value().equals(1L) &&
                            domain.getDeptBasicInfo().deptName().equals("Test Dept");
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysDeptRepository.findById(999L)).thenReturn(Mono.empty());

        sysDeptDomainRepository.load(new DeptId(999L))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewDomain() {
        SysDeptEntity entity = createTestEntity();
        entity.setId(null);

        when(sysDeptRepository.save(any(SysDeptEntity.class))).thenReturn(Mono.just(entity));

        SysDeptDomain domain = createTestDomain();
        domain.setId(null);

        sysDeptDomainRepository.save(domain)
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingDomain() {
        SysDeptEntity entity = createTestEntity();

        when(sysDeptRepository.save(any(SysDeptEntity.class))).thenReturn(Mono.just(entity));

        SysDeptDomain domain = createTestDomain();

        sysDeptDomainRepository.save(domain)
                .as(StepVerifier::create)
                .expectNext(1L)
                .verifyComplete();
    }

    private SysDeptEntity createTestEntity() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setParentId(0L);
        entity.setDeptName("Test Dept");
        entity.setSortOrder(1);
        entity.setDeptStatus(true);
        entity.setDeleteStatus(false);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(0);
        return entity;
    }

    private SysDeptDomain createTestDomain() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(1L));
        domain.setParentId(new DeptId(0L));
        domain.setDeptBasicInfo(new DeptBasicInfo("Test Dept"));
        domain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(0);
        return domain;
    }
}
