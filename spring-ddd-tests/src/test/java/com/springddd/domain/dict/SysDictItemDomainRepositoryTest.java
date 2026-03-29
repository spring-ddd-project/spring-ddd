package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictItemDomainRepositoryTest {

    @Test
    void repository_shouldExtendDomainRepository() {
        assertTrue(com.springddd.domain.DomainRepository.class.isAssignableFrom(SysDictItemDomainRepository.class));
    }

    @Test
    void repository_shouldBeInterface() {
        assertTrue(SysDictItemDomainRepository.class.isInterface());
    }

    @Test
    void repository_shouldHaveCorrectTypeParameters() {
        SysDictItemDomainRepository repository = new SysDictItemDomainRepository() {
            @Override
            public reactor.core.publisher.Mono<SysDictItemDomain> load(DictItemId aggregateRootId) {
                return reactor.core.publisher.Mono.empty();
            }

            @Override
            public reactor.core.publisher.Mono<Long> save(SysDictItemDomain aggregateRoot) {
                return reactor.core.publisher.Mono.just(1L);
            }
        };

        assertNotNull(repository);
    }
}
