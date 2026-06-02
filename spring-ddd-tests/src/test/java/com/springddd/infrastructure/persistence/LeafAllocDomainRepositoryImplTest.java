package com.springddd.infrastructure.persistence;

import com.springddd.domain.leaf.*;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafAllocDomainRepositoryImplTest {

    @Mock
    private LeafAllocRepository leafAllocRepository;

    @InjectMocks
    private LeafAllocDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        entity.setBizTag("test");
        entity.setMaxId(1000L);
        entity.setStep(100);
        entity.setDescription("desc");
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(0);

        when(leafAllocRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new LeafAllocId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getLeafAllocId().value());
                    assertEquals("test", domain.getBizTag().value());
                    assertEquals(1000L, domain.getMaxId().value());
                    assertEquals(100, domain.getStep().value());
                    assertEquals("desc", domain.getDescription().value());
                    assertEquals(1L, domain.getDeptId());
                    assertFalse(domain.getDeleteStatus());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(leafAllocRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new LeafAllocId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setBizTag(new BizTag("test"));
        domain.setMaxId(new MaxId(1000L));
        domain.setStep(new Step(100));
        domain.setDescription(new Description("desc"));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        LeafAllocEntity savedEntity = new LeafAllocEntity();
        savedEntity.setId(1L);

        when(leafAllocRepository.save(any(LeafAllocEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafAllocId(new LeafAllocId(1L));
        domain.setBizTag(new BizTag("test"));
        domain.setMaxId(new MaxId(1000L));
        domain.setStep(new Step(100));
        domain.setDescription(new Description("desc"));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        LeafAllocEntity savedEntity = new LeafAllocEntity();
        savedEntity.setId(1L);

        when(leafAllocRepository.save(any(LeafAllocEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void loadByBizTag_shouldReturnDomain_whenEntityExists() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        entity.setBizTag("test");
        entity.setMaxId(1000L);
        entity.setStep(100);

        when(leafAllocRepository.findByBizTag("test")).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.loadByBizTag("test"))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getLeafAllocId().value());
                    assertEquals("test", domain.getBizTag().value());
                })
                .verifyComplete();
    }

    @Test
    void loadByBizTag_shouldReturnEmpty_whenEntityNotFound() {
        when(leafAllocRepository.findByBizTag("test")).thenReturn(Mono.empty());

        StepVerifier.create(repository.loadByBizTag("test"))
                .verifyComplete();
    }
}
