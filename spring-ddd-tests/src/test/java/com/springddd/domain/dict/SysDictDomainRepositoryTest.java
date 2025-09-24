package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDictDomainRepositoryTest {

    @Test
    void repository_shouldExtendDomainRepository() {
        assertTrue(com.springddd.domain.DomainRepository.class.isAssignableFrom(SysDictDomainRepository.class));
    }

    @Test
    void repository_shouldBeInterface() {
        assertTrue(SysDictDomainRepository.class.isInterface());
    }

    @Test
    void repository_shouldHaveCorrectTypeParameters() {
        SysDictDomainRepository repository = new SysDictDomainRepository() {
            @Override
            public reactor.core.publisher.Mono<SysDictDomain> load(DictId aggregateRootId) {
                return reactor.core.publisher.Mono.empty();
            }

            @Override
            public reactor.core.publisher.Mono<Long> save(SysDictDomain aggregateRoot) {
                return reactor.core.publisher.Mono.just(1L);
            }
        };

        assertNotNull(repository);
    }
}
