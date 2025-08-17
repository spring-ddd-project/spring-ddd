package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenAggregateViewMapStruct {

    GenAggregateView toView(GenAggregateEntity entity);

    List<GenAggregateView> toViews(List<GenAggregateEntity> entities);
}
