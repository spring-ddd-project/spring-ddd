package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.domain.gen.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenAggregateCommandService {

    private final RepositoryFactory repositoryFactory;

    private final GenAggregateDomainFactory aggregateDomainFactory;

    private final WipeGenAggregateDomainService wipeGenAggregateDomainService;

    public Mono<Long> create(GenAggregateCommand command) {
        GenAggregateValueObject valueObject = new GenAggregateValueObject(command.getObjectName(), command.getObjectValue(), command.getObjectType());
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(command.getHasCreated());

        GenAggregateDomain domain = aggregateDomainFactory.newInstance(new InfoId(command.getInfoId()), valueObject, extendInfo);
        domain.create();
        return repositoryFactory.getGenAggregateDomainRepository().save(domain);
    }

    public Mono<Void> update(GenAggregateCommand command) {
        return repositoryFactory.getGenAggregateDomainRepository().load(new AggregateId(command.getId()))
                .flatMap(domain -> {
                    GenAggregateValueObject valueObject = new GenAggregateValueObject(command.getObjectName(), command.getObjectValue(), command.getObjectType());
                    GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(command.getHasCreated());
                    domain.update(new InfoId(command.getInfoId()), valueObject, extendInfo);
                    return repositoryFactory.getGenAggregateDomainRepository().save(domain);
                }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeGenAggregateDomainService.wipe(ids);
    }

    public Mono<Void> delete(List<Long> ids) {
        return reactor.core.publisher.Flux.fromIterable(ids)
                .flatMap(id -> repositoryFactory.getGenAggregateDomainRepository().load(new AggregateId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return repositoryFactory.getGenAggregateDomainRepository().save(domain);
                        }), com.springddd.domain.auth.SecurityUtils.concurrency())
                .then();
    }

    public Mono<Void> restore(List<Long> ids) {
        return reactor.core.publisher.Flux.fromIterable(ids)
                .flatMap(id -> repositoryFactory.getGenAggregateDomainRepository().load(new AggregateId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return repositoryFactory.getGenAggregateDomainRepository().save(domain);
                        }), com.springddd.domain.auth.SecurityUtils.concurrency())
                .then();
    }
}




























