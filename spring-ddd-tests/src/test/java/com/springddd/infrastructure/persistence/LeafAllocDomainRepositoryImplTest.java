package com.springddd.infrastructure.persistence;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
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
class LeafAllocDomainRepositoryImplTest {

    @Mock
    private LeafAllocRepository leafAllocRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private LeafAllocDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findByBizTag 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        LeafAllocId leafAllocId = new LeafAllocId("test_biz");
        LeafAllocEntity entity = new LeafAllocEntity();
        LeafAllocDomain domain = new LeafAllocDomain();

        given(leafAllocRepository.findByBizTag("test_biz")).willReturn(Mono.just(entity));
        given(entityFactory.createLeafAllocDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(leafAllocId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        LeafAllocId leafAllocId = new LeafAllocId("test_biz");

        given(leafAllocRepository.findByBizTag("test_biz")).willReturn(Mono.empty());

        StepVerifier.create(repository.load(leafAllocId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setId(1L);
        LeafAllocEntity entity = new LeafAllocEntity();
        LeafAllocEntity savedEntity = new LeafAllocEntity();
        savedEntity.setId(1L);

        given(entityFactory.createLeafAllocEntity(domain)).willReturn(entity);
        given(leafAllocRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setId(1L);

        given(leafAllocRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(leafAllocRepository).deleteById(1L);
    }
}
