package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.InitDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitDomainServiceImpl implements InitDomainService {

    @Override
    public boolean init() {
        log.info("Init ...");
        return false;
    }
}
