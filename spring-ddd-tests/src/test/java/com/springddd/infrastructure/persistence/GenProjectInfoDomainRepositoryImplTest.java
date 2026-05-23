package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.GenProjectInfoDomain;
import com.springddd.domain.gen.InfoId;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
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
class GenProjectInfoDomainRepositoryImplTest {

    @Mock
    private GenProjectInfoRepository genProjectInfoRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private GenProjectInfoDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        InfoId infoId = new InfoId(1L);
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        GenProjectInfoDomain domain = new GenProjectInfoDomain();

        given(genProjectInfoRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createGenProjectInfoDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(infoId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        InfoId infoId = new InfoId(1L);

        given(genProjectInfoRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(infoId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(1L));
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        GenProjectInfoEntity savedEntity = new GenProjectInfoEntity();
        savedEntity.setId(1L);

        given(entityFactory.createGenProjectInfoEntity(domain)).willReturn(entity);
        given(genProjectInfoRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        GenProjectInfoDomain domain = new GenProjectInfoDomain();
        domain.setId(new InfoId(1L));

        given(genProjectInfoRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(genProjectInfoRepository).deleteById(1L);
    }
}
