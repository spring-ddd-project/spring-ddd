package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.TemplateId;
import com.springddd.domain.gen.TemplateInfo;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private GenTemplateDomainRepositoryImpl genTemplateDomainRepository;

    @BeforeEach
    void setUp() {
        genTemplateDomainRepository = new GenTemplateDomainRepositoryImpl(genTemplateRepository);
    }

    @Test
    void shouldLoadTemplateById() {
        Long templateId = 1L;
        GenTemplateEntity entity = createTemplateEntity(templateId);
        when(genTemplateRepository.findById(templateId)).thenReturn(Mono.just(entity));

        Mono<GenTemplateDomain> result = genTemplateDomainRepository.load(new TemplateId(templateId));

        StepVerifier.create(result)
                .assertNext(domain -> {
                    assertNotNull(domain);
                    assertEquals(templateId, domain.getId().value());
                    assertEquals("test-template", domain.getTemplateInfo().templateName());
                    assertEquals("template content", domain.getTemplateInfo().templateContent());
                })
                .verifyComplete();

        verify(genTemplateRepository).findById(templateId);
    }

    @Test
    void shouldReturnEmptyWhenTemplateNotFound() {
        Long templateId = 999L;
        when(genTemplateRepository.findById(templateId)).thenReturn(Mono.empty());

        Mono<GenTemplateDomain> result = genTemplateDomainRepository.load(new TemplateId(templateId));

        StepVerifier.create(result)
                .verifyComplete();

        verify(genTemplateRepository).findById(templateId);
    }

    @Test
    void shouldSaveTemplateSuccessfully() {
        GenTemplateDomain domain = createGenTemplateDomain();
        GenTemplateEntity savedEntity = createTemplateEntity(1L);
        
        when(genTemplateRepository.save(any(GenTemplateEntity.class))).thenReturn(Mono.just(savedEntity));

        Mono<Long> result = genTemplateDomainRepository.save(domain);

        StepVerifier.create(result)
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();

        verify(genTemplateRepository).save(any(GenTemplateEntity.class));
    }

    @Test
    void shouldSaveTemplateWithNullId() {
        GenTemplateDomain domain = createGenTemplateDomain();
        domain.setId(null);
        
        GenTemplateEntity savedEntity = new GenTemplateEntity();
        savedEntity.setId(1L);
        when(genTemplateRepository.save(any(GenTemplateEntity.class))).thenReturn(Mono.just(savedEntity));

        Mono<Long> result = genTemplateDomainRepository.save(domain);

        StepVerifier.create(result)
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();

        verify(genTemplateRepository).save(any(GenTemplateEntity.class));
    }

    private GenTemplateEntity createTemplateEntity(Long id) {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(id);
        entity.setTemplateName("test-template");
        entity.setTemplateContent("template content");
        entity.setDeleteStatus(false);
        entity.setVersion(1L);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }

    private GenTemplateDomain createGenTemplateDomain() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new TemplateId(1L));
        domain.setTemplateInfo(new TemplateInfo("test-template", "template content"));
        domain.setDeleteStatus(false);
        domain.setVersion(1L);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        return domain;
    }
}
