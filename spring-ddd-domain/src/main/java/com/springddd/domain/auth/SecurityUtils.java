package com.springddd.domain.auth;

public class SecurityUtils {

    /**
     * Returns the recommended concurrency level based on available processors.
     * This is unrelated to authentication and is kept here for backward compatibility.
     */
    public static Integer concurrency() {
        return Runtime.getRuntime().availableProcessors() * 2;
    }

}
