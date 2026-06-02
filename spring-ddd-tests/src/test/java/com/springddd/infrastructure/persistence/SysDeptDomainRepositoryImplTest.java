package com.springddd.infrastructure.persistence;

import com.springddd.domain.dept.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysDeptDomainRepositoryImplTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @InjectMocks
    private SysDeptDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setParentId(0L);
        entity.setDeptName("研发部");
        entity.setSortOrder(1);
        entity.setDeptStatus(true);
        entity.setDeleteStatus(false);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(0);

        when(sysDeptRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new DeptId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getId().value());
                    assertEquals(0L, domain.getParentId().value());
                    assertEquals("研发部", domain.getDeptBasicInfo().deptName());
                    assertEquals(1, domain.getDeptExtendInfo().sortOrder());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysDeptRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new DeptId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(null);
        domain.setParentId(new DeptId(0L));
        domain.setDeptBasicInfo(new DeptBasicInfo("研发部"));
        domain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysDeptEntity savedEntity = new SysDeptEntity();
        savedEntity.setId(1L);

        when(sysDeptRepository.save(any(SysDeptEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setId(new DeptId(1L));
        domain.setParentId(new DeptId(0L));
        domain.setDeptBasicInfo(new DeptBasicInfo("研发部"));
        domain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysDeptEntity savedEntity = new SysDeptEntity();
        savedEntity.setId(1L);

        when(sysDeptRepository.save(any(SysDeptEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
