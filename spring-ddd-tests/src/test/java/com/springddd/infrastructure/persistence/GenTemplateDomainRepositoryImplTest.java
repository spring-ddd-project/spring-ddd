package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import com.springddd.domain.gen.TemplateInfo;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
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
@DisplayName("GenTemplateDomainRepositoryImpl Tests")
class GenTemplateDomainRepositoryImplTest {

    @Mock
    private GenTemplateRepository genTemplateRepository;

    @InjectMocks
    private GenTemplateDomainRepositoryImpl genTemplateDomainRepository;

    private GenTemplateEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new GenTemplateEntity();
        testEntity.setId(1L);
        testEntity.setTemplateName("entity模板");
        testEntity.setTemplateContent("#if(${entity})\nclass ${entity} {}\n#end");
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
        when(genTemplateRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(genTemplateDomainRepository.load(new TemplateId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getId().value()).isEqualTo(1L);
                    assertThat(domain.getTemplateInfo().templateName()).isEqualTo("entity模板");
                    assertThat(domain.getTemplateInfo().templateContent()).isEqualTo("#if(${entity})\nclass ${entity} {}\n#end");
                    assertThat(domain.getDeleteStatus()).isFalse();
                    assertThat(domain.getVersion()).isEqualTo(1);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(genTemplateRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(genTemplateDomainRepository.load(new TemplateId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(genTemplateRepository.save(any(GenTemplateEntity.class))).thenReturn(Mono.just(testEntity));

        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(new TemplateId(1L));
        domain.setTemplateInfo(new TemplateInfo("entity模板", "#if(${entity})\nclass ${entity} {}\n#end"));
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(genTemplateDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        GenTemplateEntity newEntity = new GenTemplateEntity();
        newEntity.setId(2L);
        when(genTemplateRepository.save(any(GenTemplateEntity.class))).thenReturn(Mono.just(newEntity));

        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setId(null);
        domain.setTemplateInfo(new TemplateInfo("new template", "new content"));

        StepVerifier.create(genTemplateDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
