package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenProjectInfoViewMapStruct {

    GenProjectInfoView toView(GenProjectInfoEntity entity);

    List<GenProjectInfoView> toViews(List<GenProjectInfoEntity> entities);
}
