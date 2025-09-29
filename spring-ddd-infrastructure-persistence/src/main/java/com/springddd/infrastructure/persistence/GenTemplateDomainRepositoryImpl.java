package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class GenTemplateDomainRepositoryImpl implements GenTemplateDomainRepository {

    private final GenTemplateRepository genTemplateRepository;
    private final EntityFactory entityFactory;

    @Override
    public Mono<GenTemplateDomain> load(TemplateId aggregateRootId) {
        return genTemplateRepository.findById(aggregateRootId.value())
                .map(entityFactory::createGenTemplateDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> save(GenTemplateDomain aggregateRoot) {
        GenTemplateEntity entity = entityFactory.createGenTemplateEntity(aggregateRoot);
        return genTemplateRepository.save(entity).map(GenTemplateEntity::getId);
    }
}
