package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.LinkRoleAndMenusDomainService;
import com.springddd.domain.role.RoleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkRoleAndMenusDomainServiceImpl implements LinkRoleAndMenusDomainService {


    @Override
    public Mono<Void> link(RoleId roleId, List<MenuId> menuIds) {
        return null;
    }
}
