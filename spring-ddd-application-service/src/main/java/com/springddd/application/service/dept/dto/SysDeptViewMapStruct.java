package com.springddd.application.service.dept.dto;

import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysDeptViewMapStruct {

    SysDeptView toView(SysDeptEntity entity);

    List<SysDeptView> toViews(List<SysDeptEntity> entities);
}
