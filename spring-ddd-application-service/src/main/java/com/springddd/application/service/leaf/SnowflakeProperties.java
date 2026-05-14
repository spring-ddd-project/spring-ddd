package com.springddd.application.service.leaf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "leaf.snowflake")
public class SnowflakeProperties {

    private boolean enabled = false;

    private long workerId = 0L;

    private long datacenterId = 0L;

    private long twepoch = 1288834974657L;

    private int port = 8080;
}
