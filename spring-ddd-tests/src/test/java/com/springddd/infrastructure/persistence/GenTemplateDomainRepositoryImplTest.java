package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
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
class GenTemplateDomainRepositoryImplTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    @InjectMocks
    private GenTemplateDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("模板A");
        entity.setTemplateContent("content");
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(genTemplateRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new TemplateId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getId().value());
                    assertEquals("模板A", domain.getTemplateInfo().templateName());
                    assertEquals("content", domain.getTemplateInfo().templateContent());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(genTemplateRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new TemplateId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(null);
        domain.setTemplateInfo(new TemplateInfo("模板A", "content"));
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        GenTemplateEntity savedEntity = new GenTemplateEntity();
        savedEntity.setId(1L);

        when(genTemplateRepository.save(any(GenTemplateEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new TemplateId(1L));
        domain.setTemplateInfo(new TemplateInfo("模板A", "content"));
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        GenTemplateEntity savedEntity = new GenTemplateEntity();
        savedEntity.setId(1L);

        when(genTemplateRepository.save(any(GenTemplateEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
