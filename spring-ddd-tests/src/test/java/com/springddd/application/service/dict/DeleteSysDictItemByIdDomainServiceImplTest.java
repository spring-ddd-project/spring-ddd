package com.springddd.application.service.dict;

import com.springddd.domain.dict.DictItemId;
import com.springddd.domain.dict.SysDictItemDomain;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysDictItemByIdDomainServiceImplTest {

    @Mock
    private SysDictItemDomainRepository sysDictItemDomainRepository;

    @InjectMocks
    private DeleteSysDictItemByIdDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应加载 domain 并调用 delete 和 save")
    void deleteByIds_shouldDeleteAndSave() {
        SysDictItemDomain domain = mock(SysDictItemDomain.class);
        when(sysDictItemDomainRepository.load(new DictItemId(1L))).thenReturn(Mono.just(domain));
        when(sysDictItemDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).delete();
        verify(sysDictItemDomainRepository).save(domain);
    }
}
