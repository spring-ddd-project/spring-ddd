package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenAggregateCommandService {

    private final GenAggregateDomainRepository genAggregateDomainRepository;

    private final GenAggregateDomainFactory aggregateDomainFactory;

    private final WipeGenAggregateDomainService wipeGenAggregateDomainService;

    public Mono<Long> create(GenAggregateCommand command) {
        GenAggregateValueObject valueObject = new GenAggregateValueObject(command.getObjectName(), command.getObjectValue(), command.getObjectType());
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(command.getHasCreated());

        GenAggregateDomain domain = aggregateDomainFactory.newInstance(new InfoId(command.getInfoId()), valueObject, extendInfo);
        domain.create();
        return genAggregateDomainRepository.save(domain);
    }

    public Mono<Void> update(GenAggregateCommand command) {
        return genAggregateDomainRepository.load(new AggregateId(command.getId()))
                .flatMap(domain -> {
                    GenAggregateValueObject valueObject = new GenAggregateValueObject(command.getObjectName(), command.getObjectValue(), command.getObjectType());
                    GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(command.getHasCreated());
                    domain.update(new InfoId(command.getInfoId()), valueObject, extendInfo);
                    return genAggregateDomainRepository.save(domain);
                }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeGenAggregateDomainService.wipe(ids);
    }
}
