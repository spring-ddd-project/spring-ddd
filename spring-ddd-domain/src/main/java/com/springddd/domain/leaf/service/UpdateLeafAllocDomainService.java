package com.springddd.domain.leaf.service;

import com.springddd.domain.leaf.BizTag;
import com.springddd.domain.leaf.Description;
import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.MaxId;
import com.springddd.domain.leaf.Step;
import reactor.core.publisher.Mono;

public interface UpdateLeafAllocDomainService {

    Mono<LeafAllocDomain> update(LeafAllocDomain domain, BizTag bizTag, MaxId maxId, Step step, Description description, Long deptId);
}
