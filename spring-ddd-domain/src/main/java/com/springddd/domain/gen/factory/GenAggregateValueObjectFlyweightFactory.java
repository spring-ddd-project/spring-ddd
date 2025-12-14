package com.springddd.domain.gen.factory;

import com.springddd.domain.gen.GenAggregateValueObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenAggregateValueObjectFlyweightFactory {
    private static final Map<String, GenAggregateValueObject> cache = new ConcurrentHashMap<>();

    public static GenAggregateValueObject getValueObject(String name, String value, Byte type) {
        String key = name + ":" + value + ":" + type;
        return cache.computeIfAbsent(key, k -> new GenAggregateValueObject(name, value, type));
    }
}









