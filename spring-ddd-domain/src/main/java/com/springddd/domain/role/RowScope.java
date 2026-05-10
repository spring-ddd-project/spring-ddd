package com.springddd.domain.role;

import java.util.List;

public record RowScope(List<Long> deptIds, List<Long> postIds, List<Long> userIds, Boolean self) {
}
