package com.springddd.application.service.dict.dto;

import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysDictViewMapStruct {

    SysDictView toView(SysDictEntity sysDictEntity);

    List<SysDictView> toViews(List<SysDictEntity> sysDictEntities);
}
