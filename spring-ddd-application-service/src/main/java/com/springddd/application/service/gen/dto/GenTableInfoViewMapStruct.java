package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenTableInfoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenTableInfoViewMapStruct {

    GenTableInfoView toView(GenTableInfoEntity entity);

    List<GenTableInfoView> toViews(List<GenTableInfoEntity> entities);
}
