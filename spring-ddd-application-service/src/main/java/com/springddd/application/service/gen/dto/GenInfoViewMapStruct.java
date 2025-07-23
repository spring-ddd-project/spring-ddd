package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenInfoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenInfoViewMapStruct {

    GenInfoView toView(GenInfoEntity entity);

    List<GenInfoView> toViews(List<GenInfoEntity> entities);
}
