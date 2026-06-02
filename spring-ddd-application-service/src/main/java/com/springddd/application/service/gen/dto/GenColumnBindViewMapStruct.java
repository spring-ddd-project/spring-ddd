package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenColumnBindViewMapStruct {

    GenColumnBindView toView(GenColumnBindEntity entity);

    List<GenColumnBindView> toViews(List<GenColumnBindEntity> entities);
}
