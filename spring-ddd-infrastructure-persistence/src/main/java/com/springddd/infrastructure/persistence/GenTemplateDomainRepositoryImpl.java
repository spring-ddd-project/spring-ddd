package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import com.springddd.domain.gen.TemplateInfo;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenTemplateDomainRepositoryImpl implements GenTemplateDomainRepository {

    private final GenTemplateRepository genTemplateRepository;

    @Override
    public Mono<GenTemplateDomain> load(TemplateId aggregateRootId) {
        return genTemplateRepository.findById(aggregateRootId.value())
                .map(e -> {
                    GenTemplateDomain domain = new GenTemplateDomain();

                    domain.setId(new TemplateId(e.getId()));

                    TemplateInfo info = new TemplateInfo(e.getTemplateName(), e.getTemplateContent());
                    domain.setTemplateInfo(info);

                    domain.setDeleteStatus(e.getDeleteStatus());
                    domain.setVersion(e.getVersion());
                    domain.setCreateBy(e.getCreateBy());
                    domain.setCreateTime(e.getCreateTime());
                    domain.setUpdateBy(e.getUpdateBy());
                    domain.setUpdateTime(e.getUpdateTime());
                    return domain;
                });
    }

    @Override
    public Mono<Long> save(GenTemplateDomain aggregateRoot) {
        GenTemplateEntity entity = new GenTemplateEntity();

        entity.setId(Optional.ofNullable(aggregateRoot.getId()).map(TemplateId::value).orElse(null));

        TemplateInfo info = aggregateRoot.getTemplateInfo();
        entity.setTemplateName(info.templateName());
        entity.setTemplateContent(info.templateContent());

        entity.setDeleteStatus(aggregateRoot.getDeleteStatus());
        entity.setVersion(aggregateRoot.getVersion());
        entity.setCreateBy(aggregateRoot.getCreateBy());
        entity.setCreateTime(aggregateRoot.getCreateTime());
        entity.setUpdateBy(aggregateRoot.getUpdateBy());
        entity.setUpdateTime(aggregateRoot.getUpdateTime());

        return genTemplateRepository.save(entity).map(GenTemplateEntity::getId);
    }
}
