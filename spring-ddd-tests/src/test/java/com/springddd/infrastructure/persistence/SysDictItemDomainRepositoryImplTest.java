package com.springddd.infrastructure.persistence;

import com.springddd.domain.dict.DictItemId;
import com.springddd.domain.dict.SysDictItemDomain;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
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
class SysDictItemDomainRepositoryImplTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private SysDictItemDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        DictItemId itemId = new DictItemId(1L);
        SysDictItemEntity entity = new SysDictItemEntity();
        SysDictItemDomain domain = new SysDictItemDomain();

        given(sysDictItemRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createSysDictItemDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(itemId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        DictItemId itemId = new DictItemId(1L);

        given(sysDictItemRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(itemId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(1L));
        SysDictItemEntity entity = new SysDictItemEntity();
        SysDictItemEntity savedEntity = new SysDictItemEntity();
        savedEntity.setId(1L);

        given(entityFactory.createSysDictItemEntity(domain)).willReturn(entity);
        given(sysDictItemRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setItemId(new DictItemId(1L));

        given(sysDictItemRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysDictItemRepository).deleteById(1L);
    }
}
