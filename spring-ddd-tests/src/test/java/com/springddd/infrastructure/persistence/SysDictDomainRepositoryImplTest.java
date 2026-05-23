package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.DictId;
import com.springddd.domain.dict.SysDictDomain;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
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
class SysDictDomainRepositoryImplTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private SysDictDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        DictId dictId = new DictId(1L);
        SysDictEntity entity = new SysDictEntity();
        SysDictDomain domain = new SysDictDomain();

        given(sysDictRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createSysDictDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(dictId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        DictId dictId = new DictId(1L);

        given(sysDictRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(dictId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(1L));
        SysDictEntity entity = new SysDictEntity();
        SysDictEntity savedEntity = new SysDictEntity();
        savedEntity.setId(1L);

        given(entityFactory.createSysDictEntity(domain)).willReturn(entity);
        given(sysDictRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysDictDomain domain = new SysDictDomain();
        domain.setDictId(new DictId(1L));

        given(sysDictRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysDictRepository).deleteById(1L);
    }
}
