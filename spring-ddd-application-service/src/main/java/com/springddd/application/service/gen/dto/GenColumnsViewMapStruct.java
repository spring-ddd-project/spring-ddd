package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenColumnsViewMapStruct {

    GenColumnsView toView(GenColumnsEntity entity);

    List<GenColumnsView> toViews(List<GenColumnsEntity> entities);
}
