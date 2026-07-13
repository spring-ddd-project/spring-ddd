package com.springddd.domain.role;

public enum DataScope {

    ALL(0),
    DEPT_ONLY(1),
    DEPT_AND_CHILDREN(2),
    PERSONAL(3),
    POST(4);

    private final int value;

    DataScope(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DataScope of(Integer value) {
        if (value == null) {
            return null;
        }
        for (DataScope scope : values()) {
            if (scope.value == value) {
                return scope;
            }
        }
        return null;
    }

    public static boolean isValid(Integer value) {
        return of(value) != null;
    }
}
