package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.ColumnsId;
import com.springddd.domain.gen.GenColumnsDomain;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
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
class GenColumnsDomainRepositoryImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private GenColumnsDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        ColumnsId columnsId = new ColumnsId(1L);
        GenColumnsEntity entity = new GenColumnsEntity();
        GenColumnsDomain domain = new GenColumnsDomain();

        given(genColumnsRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createGenColumnsDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(columnsId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        ColumnsId columnsId = new ColumnsId(1L);

        given(genColumnsRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(columnsId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        GenColumnsEntity entity = new GenColumnsEntity();
        GenColumnsEntity savedEntity = new GenColumnsEntity();
        savedEntity.setId(1L);

        given(entityFactory.createGenColumnsEntity(domain)).willReturn(entity);
        given(genColumnsRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));

        given(genColumnsRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(genColumnsRepository).deleteById(1L);
    }
}
