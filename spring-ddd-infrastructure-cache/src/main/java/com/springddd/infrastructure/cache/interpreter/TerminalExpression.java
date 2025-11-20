package com.springddd.infrastructure.cache.interpreter;

import java.util.Map;

public class TerminalExpression implements Expression {
    private final String key;

    public TerminalExpression(String key) {
        this.key = key;
    }

    @Override
    public String interpret(Map<String, Object> context) {
        if (context.containsKey(key)) {
            return String.valueOf(context.get(key));
        }
        return key;
    }
}
