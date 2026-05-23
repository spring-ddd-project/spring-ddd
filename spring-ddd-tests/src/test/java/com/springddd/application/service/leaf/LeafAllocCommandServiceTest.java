package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LeafAllocCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @InjectMocks
    private LeafAllocCommandService service;

    @BeforeEach
    void setUp() {
        when(repositoryFactory.getLeafAllocDomainRepository()).thenReturn(leafAllocDomainRepository);
    }

    @Test
    @DisplayName("create 应保存新 domain")
    void create_shouldSaveDomain() {
        LeafAllocCommand command = new LeafAllocCommand();
        command.setId(1L);
        command.setBizTag("test");
        command.setMaxId(100L);
        command.setStep(10);
        command.setDescription("desc");

        when(leafAllocDomainRepository.save(any(LeafAllocDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .expectNext(1L)
                .verifyComplete();

        verify(leafAllocDomainRepository).save(any(LeafAllocDomain.class));
    }

    @Test
    @DisplayName("update 应加载并更新 domain")
    void update_shouldLoadAndUpdate() {
        LeafAllocCommand command = new LeafAllocCommand();
        command.setBizTag("test");
        command.setMaxId(200L);
        command.setStep(20);
        command.setDescription("updated");

        LeafAllocDomain domain = mock(LeafAllocDomain.class);
        when(leafAllocDomainRepository.load(new LeafAllocId("test"))).thenReturn(Mono.just(domain));
        when(leafAllocDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(domain).update(any(), any());
        verify(leafAllocDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应返回 empty")
    void delete_shouldReturnEmpty() {
        StepVerifier.create(service.delete(List.of(1L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("wipe 应返回 empty")
    void wipe_shouldReturnEmpty() {
        StepVerifier.create(service.wipe(List.of(1L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("restore 应返回 empty")
    void restore_shouldReturnEmpty() {
        StepVerifier.create(service.restore(List.of(1L)))
                .verifyComplete();
    }
}
