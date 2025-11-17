package com.springddd.infrastructure.persistence.factory;

import org.springframework.data.relational.core.query.Criteria;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CriteriaFlyweightFactory {
    private static final Map<String, Criteria> cache = new ConcurrentHashMap<>();

    public static Criteria getDeleteStatusCriteria(boolean isDeleted) {
        String key = "deleteStatus:" + isDeleted;
        return cache.computeIfAbsent(key, k -> Criteria.where("delete_status").is(isDeleted));
    }
}
