package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.TemplateId;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GenTemplateDomainRepositoryImplTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private GenTemplateDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        TemplateId templateId = new TemplateId(1L);
        GenTemplateEntity entity = new GenTemplateEntity();
        GenTemplateDomain domain = new GenTemplateDomain();

        given(genTemplateRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createGenTemplateDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(templateId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        TemplateId templateId = new TemplateId(1L);

        given(genTemplateRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(templateId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new TemplateId(1L));
        GenTemplateEntity entity = new GenTemplateEntity();
        GenTemplateEntity savedEntity = new GenTemplateEntity();
        savedEntity.setId(1L);

        given(entityFactory.createGenTemplateEntity(domain)).willReturn(entity);
        given(genTemplateRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new TemplateId(1L));

        given(genTemplateRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(genTemplateRepository).deleteById(1L);
    }
}
