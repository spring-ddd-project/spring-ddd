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
        GenAggregateBasicInfo basicInfo = new GenAggregateBasicInfo(new Aggregate(command.getAggregate()), new ValueObject(command.getValueObject()));
        GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(command.getDomainMask());

        GenAggregateDomain domain = aggregateDomainFactory.newInstance(new GenProjectInfoId(command.getInfoId()), basicInfo, extendInfo);
        domain.create();
        return genAggregateDomainRepository.save(domain);
    }

    public Mono<Void> update(GenAggregateCommand command) {
        return genAggregateDomainRepository.load(new AggregateId(command.getId()))
                .flatMap(domain -> {
                    GenAggregateBasicInfo basicInfo = new GenAggregateBasicInfo(new Aggregate(command.getAggregate()), new ValueObject(command.getValueObject()));
                    GenAggregateExtendInfo extendInfo = new GenAggregateExtendInfo(command.getDomainMask());
                    domain.update(new GenProjectInfoId(command.getInfoId()), basicInfo, extendInfo);
                    return genAggregateDomainRepository.save(domain);
                }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeGenAggregateDomainService.wipe(ids);
    }
}
