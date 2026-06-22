package com.springddd.domain.leaf;
import java.time.LocalDateTime;

public record ExtendInfo(Long lastTimestamp, LocalDateTime updateTime, Boolean deleteStatus) {
}
