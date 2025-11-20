package com.springddd.infrastructure.cache.interpreter;

import java.util.Map;

public interface Expression {
    String interpret(Map<String, Object> context);
}
