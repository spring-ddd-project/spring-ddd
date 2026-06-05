package com.springddd.domain.gen;

public record GenProjectInfoExtendInfo(String requestName, String projectVersion) {
    public GenProjectInfoExtendInfo() {
        this(null, null);
    }

    public GenProjectInfoExtendInfo(String requestName) {
        this(requestName, null);
    }
}
