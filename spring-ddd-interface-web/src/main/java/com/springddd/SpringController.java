package com.springddd;


import com.springddd.domain.TestUserEntity;
import com.springddd.infrastructure.persistence.mapper.TestUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class SpringController {

    private final TestUserMapper testUserMapper;

    @GetMapping("/hi")
    public Mono<TestUserEntity> hi() {
        return testUserMapper.findById(1);
    }
}
