package com.springddd.infrastructure.persistence.factory.impl;

import com.springddd.domain.gen.GenAggregateDomainRepository;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultRepositoryFactory implements RepositoryFactory {

    private final ApplicationContext applicationContext;

    @Override
    public GenAggregateDomainRepository getGenAggregateDomainRepository() {
        return applicationContext.getBean(GenAggregateDomainRepository.class);
    }
}
