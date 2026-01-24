package com.springddd.application.service.gen.dto;

import lombok.Getter;

@Getter
public enum DatabaseType {

    VARCHAR("varchar", "String"),
    CHAR("char", "String"),
    TEXT("text", "String"),
    TINYINT("tinyint", "Boolean"),
    SMALLINT("smallint", "Short"),
    INT("int", "Integer"),
    BIGINT("bigint", "Long"),
    FLOAT("float", "Float"),
    DOUBLE("double", "Double"),
    DECIMAL("decimal", "BigDecimal"),
    NUMERIC("numeric", "BigDecimal"),
    DATE("date", "LocalDate"),
    TIME("time", "LocalTime"),
    TIMESTAMP("timestamp", "LocalDateTime"),
    DATETIME("datetime", "LocalDateTime"),
    BOOLEAN("boolean", "Boolean"),
    BIT("bit", "Boolean"),
    BINARY("binary", "byte[]"),
    VARBINARY("varbinary", "byte[]");

    private final String dbType;
    private final String javaType;

    DatabaseType(String dbType, String javaType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    public static String mapDatabaseTypeToJavaType(String dbType) {
        for (DatabaseType type : values()) {
            if (type.getDbType().equalsIgnoreCase(dbType)) {
                return type.getJavaType();
            }
        }
        return "Object";
    }
}

