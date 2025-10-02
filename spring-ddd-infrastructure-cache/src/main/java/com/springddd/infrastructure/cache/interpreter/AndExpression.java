package com.springddd.infrastructure.cache.interpreter;

import java.util.Map;

public class AndExpression implements Expression {
    private final Expression expr1;
    private final Expression expr2;

    public AndExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public String interpret(Map<String, Object> context) {
        return expr1.interpret(context) + ":" + expr2.interpret(context);
    }
}
