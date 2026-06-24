package com.springddd.application.service.gen.dto;

import com.springddd.domain.util.PageResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GenTableInfoPageResponse extends PageResponse<GenTableInfoView> {

    private String databaseName;

    public GenTableInfoPageResponse(List<GenTableInfoView> items, long total, int pageNum, int pageSize, String databaseName) {
        super(items, total, pageNum, pageSize);
        this.databaseName = databaseName;
    }
}
