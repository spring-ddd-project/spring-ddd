package com.springddd.application.service.gen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenTableInfoView implements Serializable {

    private String tableName;

    private String tableComment;

    private LocalDateTime createTime;

    private String tableCollation;
}
