package com.springddd.domain.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowScope {
    private List<Long> deptIds;
    private List<Long> postIds;
    private List<Long> userIds;
    private Boolean self;
}






















