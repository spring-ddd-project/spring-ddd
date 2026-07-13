package com.springddd.application.service.common;

import com.springddd.domain.role.DataScope;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

@Getter
public class DataScopeResult {

    private final DataScope scope;

    private final Set<String> visibleUsernames;

    public DataScopeResult(DataScope scope, Set<String> visibleUsernames) {
        this.scope = scope;
        this.visibleUsernames = visibleUsernames;
    }

    public DataScopeResult(Set<String> visibleUsernames) {
        this.scope = DataScope.PERSONAL;
        this.visibleUsernames = visibleUsernames;
    }

    public static DataScopeResult all() {
        return new DataScopeResult(DataScope.ALL, Collections.emptySet());
    }

    public boolean isAll() {
        return scope == DataScope.ALL;
    }
}
