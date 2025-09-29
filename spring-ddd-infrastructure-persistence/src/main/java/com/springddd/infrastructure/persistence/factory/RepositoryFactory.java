package com.springddd.infrastructure.persistence.factory;

import com.springddd.domain.gen.GenAggregateDomainRepository;

/**
 * Factory for repositories.
 */
public interface RepositoryFactory extends InfrastructureFactory {
    GenAggregateDomainRepository getGenAggregateDomainRepository();
}
