package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GenView implements Serializable {

    private GenProjectInfoView projectInfoView;

    private List<GenColumnsView> columnsViews;

    private List<GenAggregateView> aggregateViews;
}
