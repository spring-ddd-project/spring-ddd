package com.springddd.application.service.gen;

public class SnakeToCamelConverter {

    public static String convertToCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }

        StringBuilder camelCaseString = new StringBuilder();
        boolean nextUpperCase = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    camelCaseString.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    camelCaseString.append(Character.toLowerCase(c));
                }
            }
        }

        return camelCaseString.toString();
    }
}
