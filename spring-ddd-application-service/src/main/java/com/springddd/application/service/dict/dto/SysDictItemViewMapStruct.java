package com.springddd.application.service.dict.dto;

import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysDictItemViewMapStruct {

    SysDictItemView toView(SysDictItemEntity entity);

    List<SysDictItemView> toViews(List<SysDictItemEntity> entities);
}
