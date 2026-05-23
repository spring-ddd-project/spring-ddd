package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.GenColumnBindDomain;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
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
class GenColumnBindDomainRepositoryImplTest {

    @Mock
    private GenColumnBindRepository genColumnBindRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private GenColumnBindDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        ColumnBindId bindId = new ColumnBindId(1L);
        GenColumnBindEntity entity = new GenColumnBindEntity();
        GenColumnBindDomain domain = new GenColumnBindDomain();

        given(genColumnBindRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createGenColumnBindDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(bindId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        ColumnBindId bindId = new ColumnBindId(1L);

        given(genColumnBindRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(bindId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(new ColumnBindId(1L));
        GenColumnBindEntity entity = new GenColumnBindEntity();
        GenColumnBindEntity savedEntity = new GenColumnBindEntity();
        savedEntity.setId(1L);

        given(entityFactory.createGenColumnBindEntity(domain)).willReturn(entity);
        given(genColumnBindRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setBindId(new ColumnBindId(1L));

        given(genColumnBindRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(genColumnBindRepository).deleteById(1L);
    }
}
