package com.springddd.domain.user.strategy;

import com.springddd.domain.user.SysUserRoleDomain;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReplaceRoleAssignmentStrategyTest {

    @Test
    void testAssign() {
        ReplaceRoleAssignmentStrategy strategy = new ReplaceRoleAssignmentStrategy();
        List<SysUserRoleDomain> result = strategy.assign(1L, List.of(10L, 20L));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId().value()).isEqualTo(1L);
        assertThat(result.get(0).getRoleId().value()).isEqualTo(10L);
        assertThat(result.get(1).getUserId().value()).isEqualTo(1L);
        assertThat(result.get(1).getRoleId().value()).isEqualTo(20L);
    }

    @Test
    void testAssignEmptyList() {
        ReplaceRoleAssignmentStrategy strategy = new ReplaceRoleAssignmentStrategy();
        List<SysUserRoleDomain> result = strategy.assign(1L, List.of());
        assertThat(result).isEmpty();
    }
}
